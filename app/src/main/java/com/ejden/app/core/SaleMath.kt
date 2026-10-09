package com.ejden.app.core

import kotlin.math.roundToLong

data class CartLine(
    val productId: String,
    val productName: String,
    val quantity: Double,
    val unitPrice: Long,
    // Prix d'achat au moment de la vente (sert au calcul de la marge).
    val unitCost: Long?
) {
    val total: Long get() = (quantity * unitPrice).roundToLong()
}

data class SaleTotals(
    val subtotal: Long,
    val discount: Long,
    val total: Long,
    val paid: Long,
    val change: Long,
    val remaining: Long
)

object SaleMath {
    /** Calcule les totaux d'une vente. La remise ne dépasse jamais le sous-total. */
    fun totals(lines: List<CartLine>, discount: Long, paid: Long): SaleTotals {
        val subtotal = lines.sumOf { it.total }
        val appliedDiscount = discount.coerceIn(0L, subtotal)
        val total = subtotal - appliedDiscount
        val safePaid = paid.coerceAtLeast(0L)

        return SaleTotals(
            subtotal = subtotal,
            discount = appliedDiscount,
            total = total,
            paid = safePaid,
            change = (safePaid - total).coerceAtLeast(0L),
            remaining = (total - safePaid).coerceAtLeast(0L)
        )
    }
}
