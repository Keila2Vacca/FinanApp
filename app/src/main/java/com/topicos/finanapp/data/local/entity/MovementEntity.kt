package com.topicos.finanapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.topicos.finanapp.domain.model.Movement
import com.topicos.finanapp.domain.model.MovementType

@Entity(tableName = "movements")
data class MovementEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "amount")
    val amount: Double,

    @ColumnInfo(name = "type")
    val type: String, // "INGRESO" o "GASTO"

    @ColumnInfo(name = "category")
    val category: String,

    @ColumnInfo(name = "date")
    val date: Long,

    @ColumnInfo(name = "note")
    val note: String = ""
)

fun MovementEntity.toDomain(): Movement {
    return Movement(
        id = id,
        description = description,
        amount = amount,
        type = MovementType.fromString(type),
        category = category,
        date = date,
        note = note
    )
}

fun Movement.toEntity(): MovementEntity {
    return MovementEntity(
        id = id,
        description = description,
        amount = amount,
        type = type.value,
        category = category,
        date = date,
        note = note
    )
}
