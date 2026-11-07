package com.topicos.finanapp.presentation.statistics

import com.topicos.finanapp.domain.model.CategorySummary
import com.topicos.finanapp.domain.model.FinancialSummary

data class StatisticsUiState(
    val isLoading: Boolean = true,
    val selectedTimeRange: String = "ESTE_MES", // "ESTE_MES", "MES_ANTERIOR", "HISTORIAL"
    val rangeLabel: String = "Este mes",
    val rangePeriodDescription: String = "",
    val summary: FinancialSummary = FinancialSummary(),
    val categories: List<CategorySummary> = emptyList(),
    val errorMessage: String? = null
)
