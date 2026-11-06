package com.topicos.finanapp.presentation.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.topicos.finanapp.domain.repository.MovementRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StatisticsViewModel(
    private val repository: MovementRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatisticsUiState())
    val uiState: StateFlow<StatisticsUiState> = _uiState.asStateFlow()

    init {
        loadStatistics()
    }

    private fun loadStatistics() {
        viewModelScope.launch {
            combine(
                repository.getFinancialSummary(),
                repository.getExpenseCategories()
            ) { summary, categories ->
                StatisticsUiState(
                    isLoading = false,
                    summary = summary,
                    categories = categories
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun onTimeRangeSelected(range: String) {
        _uiState.update { it.copy(selectedTimeRange = range) }
    }
}
