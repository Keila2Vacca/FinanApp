package com.topicos.finanapp.domain.repository

import com.topicos.finanapp.domain.model.CategorySummary
import com.topicos.finanapp.domain.model.FinancialSummary
import com.topicos.finanapp.domain.model.Movement
import kotlinx.coroutines.flow.Flow

interface MovementRepository {
    fun getAllMovements(): Flow<List<Movement>>
    fun getMovementsByType(type: String): Flow<List<Movement>>
    fun getMovementsByCategory(category: String): Flow<List<Movement>>
    fun getRecentMovements(limit: Int = 5): Flow<List<Movement>>
    suspend fun getMovementById(id: Int): Movement?
    
    fun getTotalIncome(): Flow<Double>
    fun getTotalExpense(): Flow<Double>
    fun getFinancialSummary(): Flow<FinancialSummary>
    fun getExpenseCategories(): Flow<List<CategorySummary>>

    suspend fun insertMovement(movement: Movement): Long
    suspend fun updateMovement(movement: Movement)
    suspend fun deleteMovement(movement: Movement)
    suspend fun deleteMovementById(id: Int)
    suspend fun deleteAllMovements()
    suspend fun restoreExamDemoData()
}
