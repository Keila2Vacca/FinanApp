package com.topicos.finanapp.presentation.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.topicos.finanapp.FinanApplication
import com.topicos.finanapp.domain.model.AppCurrency
import com.topicos.finanapp.domain.repository.MovementRepository
import com.topicos.finanapp.utils.CurrencyFormatter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repository: MovementRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        SettingsUiState(selectedCurrency = CurrencyFormatter.getCurrency())
    )
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        observeMovementsCount()
        loadSavedCurrency()
    }

    private fun loadSavedCurrency() {
        try {
            val prefs = FinanApplication.instance.getSharedPreferences("finanapp_prefs", Context.MODE_PRIVATE)
            val savedCode = prefs.getString("selected_currency", AppCurrency.defaultCurrency.code) ?: AppCurrency.defaultCurrency.code
            val curr = AppCurrency.fromCode(savedCode)
            CurrencyFormatter.setCurrency(curr)
            _uiState.update { it.copy(selectedCurrency = curr) }
        } catch (_: Exception) {}
    }

    private fun observeMovementsCount() {
        viewModelScope.launch {
            repository.getAllMovements().collect { list ->
                _uiState.update { it.copy(totalMovements = list.size) }
            }
        }
    }

    fun setCurrencyDropdownExpanded(expanded: Boolean) {
        _uiState.update { it.copy(isCurrencyDropdownExpanded = expanded) }
    }

    fun selectCurrency(currency: AppCurrency) {
        CurrencyFormatter.setCurrency(currency)
        try {
            val prefs = FinanApplication.instance.getSharedPreferences("finanapp_prefs", Context.MODE_PRIVATE)
            prefs.edit().putString("selected_currency", currency.code).apply()
        } catch (_: Exception) {}

        _uiState.update {
            it.copy(
                selectedCurrency = currency,
                isCurrencyDropdownExpanded = false,
                message = "Moneda cambiada a ${currency.displayName}"
            )
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
