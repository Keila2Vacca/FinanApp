package com.topicos.finanapp.presentation.movement_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.topicos.finanapp.domain.repository.MovementRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MovementDetailViewModel(
    private val repository: MovementRepository,
    private val movementId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovementDetailUiState())
    val uiState: StateFlow<MovementDetailUiState> = _uiState.asStateFlow()

    init {
        loadMovement()
    }

    fun loadMovement() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val mov = repository.getMovementById(movementId)
            if (mov != null) {
                _uiState.update { it.copy(isLoading = false, movement = mov) }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "No se encontró el movimiento solicitado."
                    )
                }
            }
        }
    }

    fun showDeleteDialog() {
        _uiState.update { it.copy(showDeleteDialog = true) }
    }

    fun dismissDeleteDialog() {
        _uiState.update { it.copy(showDeleteDialog = false) }
    }

    fun confirmDelete() {
        val mov = _uiState.value.movement ?: return
        viewModelScope.launch {
            repository.deleteMovement(mov)
            _uiState.update { it.copy(showDeleteDialog = false, isDeleted = true) }
        }
    }
}
