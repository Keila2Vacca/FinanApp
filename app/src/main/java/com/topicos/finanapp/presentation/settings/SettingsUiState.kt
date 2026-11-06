package com.topicos.finanapp.presentation.settings

data class SettingsUiState(
    val userName: String = "Andrés Gómez",
    val userEmail: String = "andres.gomez@correo.com",
    val totalMovements: Int = 0,
    val activeCurrency: String = "Peso colombiano (COP)",
    val showClearHistoryDialog: Boolean = false,
    val showRestoreDialog: Boolean = false,
    val message: String? = null
)
