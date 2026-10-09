package com.ejden.app.ui.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ejden.app.core.Money
import com.ejden.app.data.db.ProductEntity
import com.ejden.app.data.repo.ProductRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductsViewModel(private val repository: ProductRepository) : ViewModel() {
    val products: StateFlow<List<ProductEntity>> =
        repository.observeAll().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /** Retourne un message d'erreur, ou null si le produit est enregistré. */
    fun add(name: String, purchase: String, sale: String, stock: String): String? {
        if (name.isBlank()) return "Indiquez le nom du produit."

        val salePrice = Money.parse(sale) ?: return "Prix de vente invalide."
        val purchasePrice = if (purchase.isBlank()) 0L else Money.parse(purchase) ?: return "Prix d'achat invalide."
        val quantity = if (stock.isBlank()) 0.0 else stock.replace(',', '.').toDoubleOrNull()

        if (quantity == null || quantity < 0 || quantity > 1_000_000) return "Stock invalide."

        viewModelScope.launch { repository.add(name, purchasePrice, salePrice, quantity) }

        return null
    }
}
