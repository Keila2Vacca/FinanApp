package com.topicos.finanapp.presentation.settings

import com.topicos.finanapp.domain.model.AppCurrency

data class SettingsUiState(
    val userName: String = "Andrés Gómez",
    val userEmail: String = "andres.gomez@correo.com",
    val totalMovements: Int = 0,
    val selectedCurrency: AppCurrency = AppCurrency.defaultCurrency,
    val isCurrencyDropdownExpanded: Boolean = false,
    val showClearHistoryDialog: Boolean = false,
    val showRestoreDialog: Boolean = false,
    val message: String? = null
)
