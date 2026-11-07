package com.topicos.finanapp.domain.model

data class CategorySummary(
    val category: String,
    val amount: Double,
    val percentage: Float
)

data class FinancialSummary(
    val balance: Double = 0.0,
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val totalMovements: Int = 0,
    val dailyExpenseAverage: Double = 0.0,
    val savingsPercentage: Int = 0,
    val topExpenseCategories: List<CategorySummary> = emptyList()
)

