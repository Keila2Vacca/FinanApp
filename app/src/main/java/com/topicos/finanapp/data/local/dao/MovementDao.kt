package com.topicos.finanapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.topicos.finanapp.data.local.entity.MovementEntity
import kotlinx.coroutines.flow.Flow

data class CategoryTotalTuple(
    val category: String,
    val total: Double
)

@Dao
interface MovementDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovement(movement: MovementEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movements: List<MovementEntity>)

    @Update
    suspend fun updateMovement(movement: MovementEntity)

    @Delete
    suspend fun deleteMovement(movement: MovementEntity)

    @Query("DELETE FROM movements WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM movements")
    suspend fun deleteAll()

    @Query("SELECT * FROM movements WHERE id = :id LIMIT 1")
    suspend fun getMovementById(id: Int): MovementEntity?

    @Query("SELECT * FROM movements ORDER BY date DESC, id DESC")
    fun getAllMovements(): Flow<List<MovementEntity>>

    @Query("SELECT * FROM movements WHERE type = :type ORDER BY date DESC, id DESC")
    fun getMovementsByType(type: String): Flow<List<MovementEntity>>

    @Query("SELECT * FROM movements WHERE category = :category ORDER BY date DESC, id DESC")
    fun getMovementsByCategory(category: String): Flow<List<MovementEntity>>

    @Query("SELECT * FROM movements ORDER BY date DESC, id DESC LIMIT :limit")
    fun getRecentMovements(limit: Int): Flow<List<MovementEntity>>

    @Query("SELECT COALESCE(SUM(amount), 0.0) FROM movements WHERE type = 'INGRESO'")
    fun getTotalIncome(): Flow<Double>

    @Query("SELECT COALESCE(SUM(amount), 0.0) FROM movements WHERE type = 'GASTO'")
    fun getTotalExpense(): Flow<Double>

    @Query("SELECT category, COALESCE(SUM(amount), 0.0) as total FROM movements WHERE type = 'GASTO' GROUP BY category ORDER BY total DESC")
    fun getExpenseByCategory(): Flow<List<CategoryTotalTuple>>

    @Query("SELECT COUNT(*) FROM movements")
    fun getMovementCount(): Flow<Int>
}

