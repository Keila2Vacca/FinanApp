package com.topicos.finanapp.presentation.dashboard

import com.topicos.finanapp.domain.model.FinancialSummary
import com.topicos.finanapp.domain.model.Movement

data class DashboardUiState(
    val isLoading: Boolean = true,
    val summary: FinancialSummary = FinancialSummary(),
    val recentMovements: List<Movement> = emptyList(),
    val errorMessage: String? = null
)
