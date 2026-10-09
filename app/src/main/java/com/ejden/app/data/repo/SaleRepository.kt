package com.ejden.app.data.repo

import androidx.room.withTransaction
import com.ejden.app.core.CartLine
import com.ejden.app.core.SaleMath
import com.ejden.app.data.db.AppDatabase
import com.ejden.app.data.db.PaymentMethod
import com.ejden.app.data.db.SaleEntity
import com.ejden.app.data.db.SaleItemEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

sealed interface SaleResult {
    data class Success(val saleId: String) : SaleResult
    data object EmptyCart : SaleResult
    data object InvalidInput : SaleResult
    data class InsufficientStock(val productName: String) : SaleResult
    // Paiement inférieur au total alors que ce n'est pas une vente à crédit.
    data object UnderPaid : SaleResult
}

class SaleRepository(private val db: AppDatabase) {
    private val sales = db.saleDao()
    private val products = db.productDao()

    fun observeRevenue(range: LongRange): Flow<Long> = sales.observeRevenue(range.first, range.last)

    fun observeCount(range: LongRange): Flow<Int> = sales.observeCount(range.first, range.last)

    /**
     * Enregistre une vente et décrémente le stock dans UNE transaction :
     * tout réussit, ou rien n'est écrit.
     */
    suspend fun recordSale(
        lines: List<CartLine>,
        discount: Long,
        method: PaymentMethod,
        paid: Long,
        customerId: String? = null,
        borrowerName: String? = null
    ): SaleResult = db.withTransaction {
        if (lines.isEmpty()) return@withTransaction SaleResult.EmptyCart

        // Refuser les valeurs invalides avant tout calcul ou changement de stock.
        val invalidLine = lines.any { line ->
            val scaledQuantity = line.quantity * 100.0
            line.productId.isBlank() ||
                !line.quantity.isFinite() ||
                line.quantity <= 0.0 ||
                kotlin.math.abs(scaledQuantity - kotlin.math.round(scaledQuantity)) > 1e-6 ||
                line.unitPrice < 0L ||
                line.unitCost?.let { it < 0L } == true
        }
        if (invalidLine || discount < 0L || paid < 0L) {
            return@withTransaction SaleResult.InvalidInput
        }

        // Toutes les vérifications avant la moindre écriture.
        val needed = lines.groupBy { it.productId }.mapValues { (_, l) -> l.sumOf { it.quantity } }

        for ((productId, quantity) in needed) {
            val product = products.getById(productId)
            val name = lines.first { it.productId == productId }.productName

            if (product == null || product.stock < quantity) {
                return@withTransaction SaleResult.InsufficientStock(name)
            }
        }

        val totals = SaleMath.totals(lines, discount, paid)

        if (method != PaymentMethod.CREDIT && totals.remaining > 0) {
            return@withTransaction SaleResult.UnderPaid
        }

        val saleId = UUID.randomUUID().toString()

        sales.insertSale(
            SaleEntity(
                id = saleId,
                createdAt = System.currentTimeMillis(),
                customerId = customerId,
                borrowerName = borrowerName?.trim()?.take(60)?.ifEmpty { null },
                subtotal = totals.subtotal,
                discount = totals.discount,
                total = totals.total,
                paymentMethod = method,
                amountPaid = totals.paid,
                change = totals.change,
                remaining = totals.remaining,
                cancelledAt = null
            )
        )
        sales.insertItems(
            lines.map {
                SaleItemEntity(
                    saleId = saleId,
                    productId = it.productId,
                    productName = it.productName,
                    quantity = it.quantity,
                    unitPrice = it.unitPrice,
                    unitCost = it.unitCost,
                    total = it.total
                )
            }
        )

        for ((productId, quantity) in needed) products.decrementStock(productId, quantity)

        SaleResult.Success(saleId)
    }
}
