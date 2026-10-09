package com.ejden.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ejden.app.core.Dates
import com.ejden.app.data.repo.SaleRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class HomeState(val revenueToday: Long = 0, val salesToday: Int = 0)

class HomeViewModel(sales: SaleRepository) : ViewModel() {
    private val today = Dates.dayRange()

    val state: StateFlow<HomeState> =
        combine(sales.observeRevenue(today), sales.observeCount(today)) { revenue, count ->
            HomeState(revenue, count)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeState())
}
