package com.ejden.app.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ejden.app.ui.common.PlaceholderScreen
import com.ejden.app.ui.home.HomeScreen
import com.ejden.app.ui.products.ProductsScreen

enum class Destination(val route: String, val label: String, val icon: ImageVector) {
    Home("home", "Accueil", Icons.Filled.Home),
    Sell("sell", "Vendre", Icons.Filled.ShoppingCart),
    Products("products", "Produits", Icons.AutoMirrored.Filled.List),
    More("more", "Plus", Icons.Filled.Menu)
}

@Composable
fun EjdenRoot() {
    val nav = rememberNavController()
    val backStack by nav.currentBackStackEntryAsState()

    Scaffold(
        bottomBar = {
            NavigationBar {
                Destination.entries.forEach { destination ->
                    NavigationBarItem(
                        selected = backStack?.destination?.hierarchy?.any { it.route == destination.route } == true,
                        onClick = {
                            nav.navigate(destination.route) {
                                popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(destination.icon, contentDescription = null) },
                        label = { Text(destination.label) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = nav,
            startDestination = Destination.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Destination.Home.route) { HomeScreen() }
            composable(Destination.Sell.route) { PlaceholderScreen("Vendre", "Prochaine étape : panier et encaissement.") }
            composable(Destination.Products.route) { ProductsScreen() }
            composable(Destination.More.route) { PlaceholderScreen("Plus", "Les autres modules arrivent ensuite.") }
        }
    }
}
