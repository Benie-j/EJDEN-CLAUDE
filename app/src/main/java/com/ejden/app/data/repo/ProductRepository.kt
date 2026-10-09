package com.ejden.app.data.repo

import com.ejden.app.data.db.ProductDao
import com.ejden.app.data.db.ProductEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class ProductRepository(private val dao: ProductDao) {
    fun observeAll(): Flow<List<ProductEntity>> = dao.observeAll()

    suspend fun add(
        name: String,
        purchasePrice: Long,
        salePrice: Long,
        stock: Double,
        category: String = "",
        unit: String = "",
        stockThreshold: Double = 0.0
    ) {
        dao.upsert(
            ProductEntity(
                id = UUID.randomUUID().toString(),
                name = name.trim().take(80),
                category = category.trim().take(40),
                unit = unit.trim().take(12),
                purchasePrice = purchasePrice,
                salePrice = salePrice,
                stock = stock,
                stockThreshold = stockThreshold,
                createdAt = System.currentTimeMillis()
            )
        )
    }
}
