package com.ejden.app

import android.app.Application
import com.ejden.app.data.db.AppDatabase
import com.ejden.app.data.repo.ProductRepository
import com.ejden.app.data.repo.SaleRepository

/** Conteneur de dépendances, créé une seule fois (injection manuelle, sans framework). */
class AppContainer(database: AppDatabase) {
    val productRepository = ProductRepository(database.productDao())
    val saleRepository = SaleRepository(database)
}

class EjdenApp : Application() {
    val container: AppContainer by lazy { AppContainer(AppDatabase.create(this)) }
}
