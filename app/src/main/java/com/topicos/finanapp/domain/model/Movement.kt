package com.topicos.finanapp.domain.model

data class Movement(
    val id: Int = 0,
    val description: String,
    val amount: Double,
    val type: MovementType,
    val category: String,
    val date: Long = System.currentTimeMillis(),
    val note: String = ""
) {
    val isIncome: Boolean get() = type == MovementType.INGRESO
    val isExpense: Boolean get() = type == MovementType.GASTO
}
