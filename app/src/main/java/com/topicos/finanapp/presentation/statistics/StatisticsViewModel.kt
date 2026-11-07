package com.topicos.finanapp.presentation.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.topicos.finanapp.domain.model.CategorySummary
import com.topicos.finanapp.domain.model.FinancialSummary
import com.topicos.finanapp.domain.model.Movement
import com.topicos.finanapp.domain.repository.MovementRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class StatisticsViewModel(
    private val repository: MovementRepository
) : ViewModel() {

    private val _selectedTimeRange = MutableStateFlow("ESTE_MES")
    private val _uiState = MutableStateFlow(StatisticsUiState())
    val uiState: StateFlow<StatisticsUiState> = _uiState.asStateFlow()

    init {
        loadStatistics()
    }

    private fun loadStatistics() {
        viewModelScope.launch {
            combine(
                repository.getAllMovements(),
                _selectedTimeRange
            ) { allMovements, timeRange ->
                calculateStatisticsForRange(allMovements, timeRange)
            }.collect { calculatedState ->
                _uiState.value = calculatedState
            }
        }
    }

    fun onTimeRangeSelected(range: String) {
        _selectedTimeRange.value = range
    }

    private fun calculateStatisticsForRange(
        allMovements: List<Movement>,
        range: String
    ): StatisticsUiState {
        val locale = Locale("es", "CO")
        val nowCal = Calendar.getInstance()

        val (filteredMovements, label, desc) = when (range) {
            "ESTE_MES" -> {
                val startOfMonth = Calendar.getInstance().apply {
                    set(Calendar.DAY_OF_MONTH, 1)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.timeInMillis

                val endOfMonth = Calendar.getInstance().apply {
                    set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
                    set(Calendar.HOUR_OF_DAY, 23)
                    set(Calendar.MINUTE, 59)
                    set(Calendar.SECOND, 59)
                    set(Calendar.MILLISECOND, 999)
                }.timeInMillis

                val monthName = SimpleDateFormat("MMMM yyyy", locale).format(nowCal.time)
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }

                val filtered = allMovements.filter { it.date in startOfMonth..endOfMonth }
                Triple(filtered, "Este mes", monthName)
            }

            "MES_ANTERIOR" -> {
                val prevMonthCal = Calendar.getInstance().apply {
                    add(Calendar.MONTH, -1)
                }

                val startOfPrevMonth = Calendar.getInstance().apply {
                    add(Calendar.MONTH, -1)
                    set(Calendar.DAY_OF_MONTH, 1)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.timeInMillis

                val endOfPrevMonth = Calendar.getInstance().apply {
                    add(Calendar.MONTH, -1)
                    set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
                    set(Calendar.HOUR_OF_DAY, 23)
                    set(Calendar.MINUTE, 59)
                    set(Calendar.SECOND, 59)
                    set(Calendar.MILLISECOND, 999)
                }.timeInMillis

                val prevMonthName = SimpleDateFormat("MMMM yyyy", locale).format(prevMonthCal.time)
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }

                val filtered = allMovements.filter { it.date in startOfPrevMonth..endOfPrevMonth }
                Triple(filtered, "Mes anterior", prevMonthName)
            }

            else -> {
                // "HISTORIAL"
                Triple(allMovements, "Historial", "Todos los movimientos registrados")
            }
        }

        val income = filteredMovements.filter { it.isIncome }.sumOf { it.amount }
        val expense = filteredMovements.filter { it.isExpense }.sumOf { it.amount }
        val balance = income - expense
        val count = filteredMovements.size

        val savingsPct = if (income > 0.0) {
            (((income - expense) / income) * 100).toInt().coerceAtLeast(0)
        } else 0

        val daysInPeriod = if (range == "HISTORIAL") {
            if (filteredMovements.isNotEmpty()) {
                val oldest = filteredMovements.minOf { it.date }
                val newest = filteredMovements.maxOf { it.date }
                val diffDays = ((newest - oldest) / (1000 * 60 * 60 * 24)).coerceAtLeast(1)
                diffDays.toDouble()
            } else 30.0
        } else {
            30.0
        }

        val dailyAvg = if (expense > 0.0) expense / daysInPeriod else 0.0

        val expenseByCat = filteredMovements.filter { it.isExpense }
            .groupBy { it.category }
            .mapValues { entry -> entry.value.sumOf { it.amount } }
            .toList()
            .sortedByDescending { it.second }
            .map { (cat, total) ->
                val pct = if (expense > 0.0) ((total / expense) * 100).toFloat() else 0f
                CategorySummary(
                    category = cat,
                    amount = total,
                    percentage = pct
                )
            }

        val summary = FinancialSummary(
            balance = balance,
            totalIncome = income,
            totalExpense = expense,
            totalMovements = count,
            dailyExpenseAverage = dailyAvg,
            savingsPercentage = savingsPct,
            topExpenseCategories = expenseByCat
        )

        return StatisticsUiState(
            isLoading = false,
            selectedTimeRange = range,
            rangeLabel = label,
            rangePeriodDescription = desc,
            summary = summary,
            categories = expenseByCat
        )
    }
}
