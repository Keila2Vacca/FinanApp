package com.topicos.finanapp.presentation.movement_register

import com.topicos.finanapp.domain.model.MovementCategory
import com.topicos.finanapp.domain.model.MovementType

data class MovementRegisterUiState(
    val movementId: Int? = null,
    val isEditing: Boolean = false,
    val type: MovementType = MovementType.GASTO,
    val amountInput: String = "",
    val description: String = "",
    val category: String = MovementCategory.OTROS.id,
    val dateMillis: Long = System.currentTimeMillis(),
    val note: String = "",
    val isSaving: Boolean = false,
    val isSavedSuccess: Boolean = false,
    val errorMessage: String? = null
)

