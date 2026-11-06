package com.topicos.finanapp.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.topicos.finanapp.domain.repository.MovementRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repository: MovementRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        observeMovementsCount()
    }

    private fun observeMovementsCount() {
        viewModelScope.launch {
            repository.getAllMovements().collect { list ->
                _uiState.update { it.copy(totalMovements = list.size) }
            }
        }
    }

    fun requestClearHistory() {
        _uiState.update { it.copy(showClearHistoryDialog = true) }
    }

    fun dismissClearHistory() {
        _uiState.update { it.copy(showClearHistoryDialog = false) }
    }

    fun confirmClearHistory() {
        viewModelScope.launch {
            repository.deleteAllMovements()
            _uiState.update {
                it.copy(
                    showClearHistoryDialog = false,
                    message = "Se ha borrado todo el historial de movimientos."
                )
            }
        }
    }

    fun requestRestoreExampleData() {
        _uiState.update { it.copy(showRestoreDialog = true) }
    }

    fun dismissRestoreDialog() {
        _uiState.update { it.copy(showRestoreDialog = false) }
    }

    fun confirmRestoreExampleData() {
        viewModelScope.launch {
            repository.restoreExamDemoData()
            _uiState.update {
                it.copy(
                    showRestoreDialog = false,
                    message = "Datos del examen restaurados exitosamente ($2.500.000, $350.000, $150.000)."
                )
            }
        }
    }
}
