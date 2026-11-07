package com.topicos.finanapp.presentation.movement_register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.topicos.finanapp.domain.model.Movement
import com.topicos.finanapp.domain.model.MovementCategory
import com.topicos.finanapp.domain.model.MovementType
import com.topicos.finanapp.domain.repository.MovementRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MovementRegisterViewModel(
    private val repository: MovementRepository,
    movementId: Int? = null,
    initialType: String = "GASTO"
) : ViewModel() {

    private val defaultType = MovementType.fromString(initialType)

    private val _uiState = MutableStateFlow(
        MovementRegisterUiState(
            movementId = movementId,
            isEditing = movementId != null && movementId > 0,
            type = defaultType,
            category = MovementCategory.getDefaultCategoryForType(defaultType)
        )
    )
    val uiState: StateFlow<MovementRegisterUiState> = _uiState.asStateFlow()

    init {
        if (movementId != null && movementId > 0) {
            loadExistingMovement(movementId)
        }
    }

    private fun loadExistingMovement(id: Int) {
        viewModelScope.launch {
            val movement = repository.getMovementById(id)
            if (movement != null) {
                _uiState.update {
                    it.copy(
                        movementId = movement.id,
                        isEditing = true,
                        type = movement.type,
                        amountInput = movement.amount.toLong().toString(),
                        description = movement.description,
                        category = movement.category,
                        dateMillis = movement.date,
                        note = movement.note
                    )
                }
            }
        }
    }

    fun onTypeChanged(type: MovementType) {
        _uiState.update { current ->
            val validCategories = MovementCategory.getCategoriesForType(type)
            val newCategory = if (validCategories.any { it.id.equals(current.category, ignoreCase = true) }) {
                current.category
            } else {
                MovementCategory.getDefaultCategoryForType(type)
            }
            current.copy(type = type, category = newCategory, errorMessage = null)
        }
    }

    fun onAmountChanged(input: String) {
        // Guardar únicamente los dígitos numéricos
        val clean = input.filter { it.isDigit() }
        _uiState.update { it.copy(amountInput = clean, errorMessage = null) }
    }

    fun onDescriptionChanged(desc: String) {
        _uiState.update { it.copy(description = desc, errorMessage = null) }
    }

    fun onCategorySelected(category: String) {
        _uiState.update { it.copy(category = category, errorMessage = null) }
    }

    fun onDateChanged(millis: Long) {
        _uiState.update { it.copy(dateMillis = millis) }
    }

    fun onNoteChanged(note: String) {
        _uiState.update { it.copy(note = note) }
    }

    fun saveMovement() {
        val currentState = _uiState.value

        // Validación de descripción
        if (currentState.description.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Por favor ingresa una descripción para el movimiento.") }
            return
        }

        // Validación de monto
        val amount = currentState.amountInput.toDoubleOrNull()
        if (amount == null || amount <= 0.0) {
            _uiState.update { it.copy(errorMessage = "El valor del movimiento debe ser un número mayor a cero.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null) }
            try {
                val movement = Movement(
                    id = currentState.movementId ?: 0,
                    description = currentState.description.trim(),
                    amount = amount,
                    type = currentState.type,
                    category = currentState.category,
                    date = currentState.dateMillis,
                    note = currentState.note.trim()
                )

                if (currentState.isEditing) {
                    repository.updateMovement(movement)
                } else {
                    repository.insertMovement(movement)
                }

                _uiState.update { it.copy(isSaving = false, isSavedSuccess = true) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = "Error al guardar: ${e.localizedMessage ?: "Intenta nuevamente"}"
                    )
                }
            }
        }
    }
}
