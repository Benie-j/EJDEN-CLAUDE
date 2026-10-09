package com.ejden.app.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ejden.app.core.Money
import com.ejden.app.ui.AppViewModelProvider

@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Aujourd'hui", style = MaterialTheme.typography.headlineSmall)

        Card(Modifier.fillMaxWidth().padding(top = 12.dp)) {
            Column(Modifier.padding(16.dp)) {
                Text("Chiffre d'affaires", style = MaterialTheme.typography.labelMedium)
                Text(Money.format(state.revenueToday), style = MaterialTheme.typography.headlineMedium)
                Text(
                    "${state.salesToday} vente${if (state.salesToday > 1) "s" else ""}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
