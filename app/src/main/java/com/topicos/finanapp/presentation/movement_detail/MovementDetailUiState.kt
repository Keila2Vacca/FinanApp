package com.topicos.finanapp.presentation.movement_detail

import com.topicos.finanapp.domain.model.Movement

data class MovementDetailUiState(
    val isLoading: Boolean = true,
    val movement: Movement? = null,
    val showDeleteDialog: Boolean = false,
    val isDeleted: Boolean = false,
    val errorMessage: String? = null
)

