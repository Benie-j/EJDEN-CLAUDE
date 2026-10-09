package com.ejden.app.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products ORDER BY name COLLATE NOCASE")
    fun observeAll(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getById(id: String): ProductEntity?

    @Upsert
    suspend fun upsert(product: ProductEntity)

    @Query("UPDATE products SET stock = stock - :quantity WHERE id = :id")
    suspend fun decrementStock(id: String, quantity: Double)

    @Query("DELETE FROM products WHERE id = :id")
    suspend fun delete(id: String)
}

@Dao
interface ClientDao {
    @Query("SELECT * FROM clients ORDER BY name COLLATE NOCASE")
    fun observeAll(): Flow<List<ClientEntity>>

    @Upsert
    suspend fun upsert(client: ClientEntity)
}

@Dao
interface SaleDao {
    @Insert
    suspend fun insertSale(sale: SaleEntity)

    @Insert
    suspend fun insertItems(items: List<SaleItemEntity>)

    @Query("SELECT COALESCE(SUM(total), 0) FROM sales WHERE cancelledAt IS NULL AND createdAt BETWEEN :from AND :to")
    fun observeRevenue(from: Long, to: Long): Flow<Long>

    @Query("SELECT COUNT(*) FROM sales WHERE cancelledAt IS NULL AND createdAt BETWEEN :from AND :to")
    fun observeCount(from: Long, to: Long): Flow<Int>
}
