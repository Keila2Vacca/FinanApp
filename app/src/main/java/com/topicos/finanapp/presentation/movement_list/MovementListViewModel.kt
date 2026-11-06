package com.topicos.finanapp.presentation.movement_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.topicos.finanapp.domain.model.Movement
import com.topicos.finanapp.domain.model.MovementType
import com.topicos.finanapp.domain.repository.MovementRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MovementListViewModel(
    private val repository: MovementRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovementListUiState())
    val uiState: StateFlow<MovementListUiState> = _uiState.asStateFlow()

    init {
        observeMovements()
    }

    private fun observeMovements() {
        viewModelScope.launch {
            combine(
                repository.getAllMovements(),
                repository.getFinancialSummary()
            ) { allList, summary ->
                Pair(allList, summary)
            }.collect { (allList, summary) ->
                _uiState.update { current ->
                    current.copy(
                        isLoading = false,
                        movements = allList,
                        summary = summary,
                        filteredMovements = filterList(allList, current.searchQuery, current.selectedFilter)
                    )
                }
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { current ->
            current.copy(
                searchQuery = query,
                filteredMovements = filterList(current.movements, query, current.selectedFilter)
            )
        }
    }

    fun onFilterChanged(filter: String) {
        _uiState.update { current ->
            current.copy(
                selectedFilter = filter,
                filteredMovements = filterList(current.movements, current.searchQuery, filter)
            )
        }
    }

    fun requestDeleteMovement(movement: Movement) {
        _uiState.update { it.copy(movementToDelete = movement) }
    }

    fun dismissDeleteDialog() {
        _uiState.update { it.copy(movementToDelete = null) }
    }

    fun confirmDeleteMovement() {
        val toDelete = _uiState.value.movementToDelete ?: return
        viewModelScope.launch {
            repository.deleteMovement(toDelete)
            _uiState.update { it.copy(movementToDelete = null) }
        }
    }

    private fun filterList(
        list: List<Movement>,
        query: String,
        typeFilter: String
    ): List<Movement> {
        return list.filter { movement ->
            // Filtro por tipo
            val matchesType = when (typeFilter.uppercase()) {
                "INGRESOS" -> movement.type == MovementType.INGRESO
                "GASTOS" -> movement.type == MovementType.GASTO
                else -> true
            }

            // Filtro por búsqueda (descripción o categoría)
            val matchesQuery = query.isBlank() ||
                    movement.description.contains(query, ignoreCase = true) ||
                    movement.category.contains(query, ignoreCase = true)

            matchesType && matchesQuery
        }
    }
}
