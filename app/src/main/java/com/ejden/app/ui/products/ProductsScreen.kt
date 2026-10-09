package com.ejden.app.ui.products

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ejden.app.core.Money
import com.ejden.app.ui.AppViewModelProvider

@Composable
fun ProductsScreen(viewModel: ProductsViewModel = viewModel(factory = AppViewModelProvider.Factory)) {
    val products by viewModel.products.collectAsStateWithLifecycle()
    var showDialog by rememberSaveable { mutableStateOf(false) }

    Box(Modifier.fillMaxSize()) {
        if (products.isEmpty()) {
            Text(
                "Aucun produit. Touchez + pour en ajouter.",
                modifier = Modifier.align(Alignment.Center).padding(24.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            LazyColumn(Modifier.fillMaxSize()) {
                items(products, key = { it.id }) { product ->
                    ListItem(
                        headlineContent = { Text(product.name) },
                        supportingContent = { Text("Stock : ${Money.quantity(product.stock)}") },
                        trailingContent = { Text(Money.format(product.salePrice)) }
                    )
                    HorizontalDivider()
                }
            }
        }

        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Ajouter un produit")
        }
    }

    if (showDialog) {
        AddProductDialog(
            onDismiss = { showDialog = false },
            onSave = { name, purchase, sale, stock ->
                val error = viewModel.add(name, purchase, sale, stock)

                if (error == null) showDialog = false

                error
            }
        )
    }
}

@Composable
private fun AddProductDialog(
    onDismiss: () -> Unit,
    onSave: (name: String, purchase: String, sale: String, stock: String) -> String?
) {
    var name by rememberSaveable { mutableStateOf("") }
    var purchase by rememberSaveable { mutableStateOf("") }
    var sale by rememberSaveable { mutableStateOf("") }
    var stock by rememberSaveable { mutableStateOf("") }
    var error by rememberSaveable { mutableStateOf<String?>(null) }
    val number = KeyboardOptions(keyboardType = KeyboardType.Number)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nouveau produit") },
        text = {
            Column {
                OutlinedTextField(name, { name = it }, label = { Text("Nom") }, singleLine = true)
                OutlinedTextField(purchase, { purchase = it }, label = { Text("Prix d'achat (FCFA)") }, singleLine = true, keyboardOptions = number)
                OutlinedTextField(sale, { sale = it }, label = { Text("Prix de vente (FCFA)") }, singleLine = true, keyboardOptions = number)
                OutlinedTextField(stock, { stock = it }, label = { Text("Stock") }, singleLine = true, keyboardOptions = number)

                error?.let { Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp)) }
            }
        },
        confirmButton = { TextButton(onClick = { error = onSave(name, purchase, sale, stock) }) { Text("Enregistrer") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Annuler") } }
    )
}
