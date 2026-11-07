package com.topicos.finanapp.data.repository

import com.topicos.finanapp.data.local.MovementDatabase
import com.topicos.finanapp.data.local.dao.MovementDao
import com.topicos.finanapp.data.local.entity.toDomain
import com.topicos.finanapp.data.local.entity.toEntity
import com.topicos.finanapp.domain.model.CategorySummary
import com.topicos.finanapp.domain.model.FinancialSummary
import com.topicos.finanapp.domain.model.Movement
import com.topicos.finanapp.domain.repository.MovementRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class MovementRepositoryImpl(
    private val movementDao: MovementDao
) : MovementRepository {

    override fun getAllMovements(): Flow<List<Movement>> {
        return movementDao.getAllMovements()
            .map { list -> list.map { it.toDomain() } }
            .flowOn(Dispatchers.IO)
    }

    override fun getMovementsByType(type: String): Flow<List<Movement>> {
        return movementDao.getMovementsByType(type)
            .map { list -> list.map { it.toDomain() } }
            .flowOn(Dispatchers.IO)
    }

    override fun getMovementsByCategory(category: String): Flow<List<Movement>> {
        return movementDao.getMovementsByCategory(category)
            .map { list -> list.map { it.toDomain() } }
            .flowOn(Dispatchers.IO)
    }

    override fun getRecentMovements(limit: Int): Flow<List<Movement>> {
        return movementDao.getRecentMovements(limit)
            .map { list -> list.map { it.toDomain() } }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun getMovementById(id: Int): Movement? = withContext(Dispatchers.IO) {
        movementDao.getMovementById(id)?.toDomain()
    }

    override fun getTotalIncome(): Flow<Double> {
        return movementDao.getTotalIncome().flowOn(Dispatchers.IO)
    }

    override fun getTotalExpense(): Flow<Double> {
        return movementDao.getTotalExpense().flowOn(Dispatchers.IO)
    }

    override fun getFinancialSummary(): Flow<FinancialSummary> {
        return combine(
            movementDao.getTotalIncome(),
            movementDao.getTotalExpense(),
            movementDao.getMovementCount(),
            movementDao.getExpenseByCategory()
        ) { income, expense, count, expenseByCat ->
            val balance = income - expense
            val savingsPct = if (income > 0.0) {
                (((income - expense) / income) * 100).toInt().coerceAtLeast(0)
            } else 0
            val dailyAvg = if (expense > 0.0) expense / 30.0 else 0.0

            val topCategories = expenseByCat.map { tuple ->
                val pct = if (expense > 0.0) {
                    ((tuple.total / expense) * 100).toFloat()
                } else 0f
                CategorySummary(
                    category = tuple.category,
                    amount = tuple.total,
                    percentage = pct
                )
            }

            FinancialSummary(
                balance = balance,
                totalIncome = income,
                totalExpense = expense,
                totalMovements = count,
                dailyExpenseAverage = dailyAvg,
                savingsPercentage = savingsPct,
                topExpenseCategories = topCategories
            )
        }.flowOn(Dispatchers.IO)
    }

    override fun getExpenseCategories(): Flow<List<CategorySummary>> {
        return combine(
            movementDao.getTotalExpense(),
            movementDao.getExpenseByCategory()
        ) { totalExpense, list ->
            list.map { tuple ->
                val pct = if (totalExpense > 0.0) {
                    ((tuple.total / totalExpense) * 100).toFloat()
                } else 0f
                CategorySummary(
                    category = tuple.category,
                    amount = tuple.total,
                    percentage = pct
                )
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun insertMovement(movement: Movement): Long = withContext(Dispatchers.IO) {
        movementDao.insertMovement(movement.toEntity())
    }

    override suspend fun updateMovement(movement: Movement) = withContext(Dispatchers.IO) {
        movementDao.updateMovement(movement.toEntity())
    }

    override suspend fun deleteMovement(movement: Movement) = withContext(Dispatchers.IO) {
        movementDao.deleteMovement(movement.toEntity())
    }

    override suspend fun deleteMovementById(id: Int) = withContext(Dispatchers.IO) {
        movementDao.deleteById(id)
    }

    override suspend fun deleteAllMovements() = withContext(Dispatchers.IO) {
        movementDao.deleteAll()
    }

    override suspend fun restoreExamDemoData() = withContext(Dispatchers.IO) {
        movementDao.deleteAll()
        MovementDatabase.populateInitialData(movementDao)
    }
}

