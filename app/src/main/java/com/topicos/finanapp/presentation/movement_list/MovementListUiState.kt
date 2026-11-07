package com.topicos.finanapp.presentation.movement_list

import com.topicos.finanapp.domain.model.FinancialSummary
import com.topicos.finanapp.domain.model.Movement

data class MovementListUiState(
    val isLoading: Boolean = true,
    val movements: List<Movement> = emptyList(),
    val filteredMovements: List<Movement> = emptyList(),
    val summary: FinancialSummary = FinancialSummary(),
    val searchQuery: String = "",
    val selectedFilter: String = "TODOS", // "TODOS", "INGRESOS", "GASTOS"
    val movementToDelete: Movement? = null,
    val errorMessage: String? = null
)

