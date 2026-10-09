package com.ejden.app.data.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

enum class PaymentMethod { CASH, MOBILE_MONEY, CARD, OTHER, CREDIT }

// Identifiants texte (UUID) : ils permettent de reprendre les données de la version web.
@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    val name: String,
    val category: String,
    val unit: String,
    val purchasePrice: Long,
    val salePrice: Long,
    val stock: Double,
    val stockThreshold: Double,
    val createdAt: Long
)

@Entity(tableName = "clients")
data class ClientEntity(
    @PrimaryKey val id: String,
    val name: String,
    val phone: String,
    val createdAt: Long
)

@Entity(
    tableName = "sales",
    indices = [Index("createdAt"), Index("customerId")]
)
data class SaleEntity(
    @PrimaryKey val id: String,
    val createdAt: Long,
    val customerId: String?,
    val borrowerName: String?,
    val subtotal: Long,
    val discount: Long,
    val total: Long,
    val paymentMethod: PaymentMethod,
    val amountPaid: Long,
    val change: Long,
    val remaining: Long,
    val cancelledAt: Long?
)

@Entity(
    tableName = "sale_items",
    foreignKeys = [
        ForeignKey(
            entity = SaleEntity::class,
            parentColumns = ["id"],
            childColumns = ["saleId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("saleId"), Index("productId")]
)
data class SaleItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val saleId: String,
    val productId: String,
    // Copie : l'historique reste lisible si le produit est supprimé.
    val productName: String,
    val quantity: Double,
    val unitPrice: Long,
    val unitCost: Long?,
    val total: Long
)
