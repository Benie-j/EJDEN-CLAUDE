package com.ejden.app.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.ejden.app.AppContainer
import com.ejden.app.EjdenApp
import com.ejden.app.ui.home.HomeViewModel
import com.ejden.app.ui.products.ProductsViewModel

private fun CreationExtras.container(): AppContainer =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as EjdenApp).container

/** Fabrique unique des ViewModels : chaque nouvel écran s'y déclare. */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer { HomeViewModel(container().saleRepository) }
        initializer { ProductsViewModel(container().productRepository) }
    }
}
