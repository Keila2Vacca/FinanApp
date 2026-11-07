package com.topicos.finanapp.presentation.statistics

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.topicos.finanapp.domain.model.CategorySummary
import com.topicos.finanapp.presentation.common.components.FinanTopBar
import com.topicos.finanapp.presentation.common.components.SummaryStatCard
import com.topicos.finanapp.presentation.common.components.getCategoryIconAndColor
import com.topicos.finanapp.presentation.common.theme.FinanBackground
import com.topicos.finanapp.presentation.common.theme.FinanCardBorder
import com.topicos.finanapp.presentation.common.theme.FinanExpenseYellow
import com.topicos.finanapp.presentation.common.theme.FinanGreen
import com.topicos.finanapp.presentation.common.theme.FinanNavy
import com.topicos.finanapp.presentation.common.theme.FinanTextMuted
import com.topicos.finanapp.presentation.common.theme.FinanTextPrimary
import com.topicos.finanapp.presentation.common.theme.FinanTextSecondary
import com.topicos.finanapp.utils.CurrencyFormatter

@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val activeCurrency by CurrencyFormatter.currentCurrency.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FinanBackground)
    ) {
        FinanTopBar(subtitle = "Estadísticas")

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = FinanGreen)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Título, Subtítulo y Período Activo
                item {
                    Column {
                        Text(
                            text = "Estadísticas",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = FinanTextPrimary
                            )
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Entiende en qué se va tu dinero y cómo evoluciona tu balance.",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = FinanTextSecondary
                                )
                            )
                            if (uiState.rangePeriodDescription.isNotBlank()) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(FinanGreen.copy(alpha = 0.12f))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = uiState.rangePeriodDescription,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = FinanGreen,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                // Filtros de Rango Temporal: [Este mes] [Mes anterior] [Historial]
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TimePill(
                            title = "Este mes",
                            isSelected = uiState.selectedTimeRange == "ESTE_MES",
                            onClick = { viewModel.onTimeRangeSelected("ESTE_MES") }
                        )
                        TimePill(
                            title = "Mes anterior",
                            isSelected = uiState.selectedTimeRange == "MES_ANTERIOR",
                            onClick = { viewModel.onTimeRangeSelected("MES_ANTERIOR") }
                        )
                        TimePill(
                            title = "Historial",
                            isSelected = uiState.selectedTimeRange == "HISTORIAL",
                            onClick = { viewModel.onTimeRangeSelected("HISTORIAL") }
                        )
                    }
                }

                // Fila de 4 tarjetas estadísticas (Ingresos, Gastos, Balance, Gasto Diario)
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        SummaryStatCard(
                            title = "Ingresos",
                            amountFormatted = CurrencyFormatter.format(uiState.summary.totalIncome, activeCurrency),
                            amountColor = FinanGreen,
                            isIncomeArrow = true,
                            modifier = Modifier.weight(1f)
                        )
                        SummaryStatCard(
                            title = "Gastos",
                            amountFormatted = CurrencyFormatter.format(uiState.summary.totalExpense, activeCurrency),
                            amountColor = FinanTextPrimary,
                            isIncomeArrow = false,
                            modifier = Modifier.weight(1f)
                        )
                        SummaryStatCard(
                            title = "Balance",
                            amountFormatted = CurrencyFormatter.format(uiState.summary.balance, activeCurrency),
                            amountColor = FinanNavy,
                            modifier = Modifier.weight(1f)
                        )
                        SummaryStatCard(
                            title = "Gasto diario",
                            amountFormatted = CurrencyFormatter.format(uiState.summary.dailyExpenseAverage, activeCurrency),
                            amountColor = FinanTextPrimary,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Si no hay movimientos en el período seleccionado
                if (uiState.summary.totalMovements == 0) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(1.dp, FinanCardBorder)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(36.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No se encontraron movimientos registrados para ${uiState.rangeLabel.lowercase()} (${uiState.rangePeriodDescription}).",
                                    style = MaterialTheme.typography.bodyMedium.copy(color = FinanTextSecondary)
                                )
                            }
                        }
                    }
                } else {
                    // Fila de Gráficas: Ingresos vs Gastos + Gastos por Categoría
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            // Gráfica de Barras: Ingresos vs Gastos
                            Card(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                border = BorderStroke(1.dp, FinanCardBorder)
                            ) {
                                Column(modifier = Modifier.padding(20.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Ingresos vs gastos",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = FinanTextPrimary
                                            )
                                        )
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(Color(0xFFF1F5F9))
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = "${uiState.summary.totalMovements} mov.",
                                                style = MaterialTheme.typography.labelSmall.copy(color = FinanTextSecondary)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(20.dp))

                                    IncomeVsExpenseBarChart(
                                        income = uiState.summary.totalIncome,
                                        expense = uiState.summary.totalExpense
                                    )
                                }
                            }

                            // Gráfica de Dona: Gastos por Categoría
                            Card(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                border = BorderStroke(1.dp, FinanCardBorder)
                            ) {
                                Column(modifier = Modifier.padding(20.dp)) {
                                    Text(
                                        text = "Gastos por categoría",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = FinanTextPrimary
                                        )
                                    )

                                    Spacer(modifier = Modifier.height(20.dp))

                                    DonutExpenseChart(categories = uiState.categories)
                                }
                            }
                        }
                    }

                    // Tarjeta Inferior: Categorías con mayor gasto
                    item {
                        val topCategoryName = uiState.categories.firstOrNull()?.category ?: "Sin registros"
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(1.dp, FinanCardBorder)
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Categorías con mayor gasto",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = FinanTextPrimary
                                        )
                                    )

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(FinanGreen.copy(alpha = 0.12f))
                                            .padding(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "Top $topCategoryName",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = FinanGreen
                                            )
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                if (uiState.categories.isEmpty()) {
                                    Text(
                                        text = "No hay gastos registrados en este período.",
                                        style = MaterialTheme.typography.bodyMedium.copy(color = FinanTextMuted),
                                        modifier = Modifier.padding(vertical = 12.dp)
                                    )
                                } else {
                                    uiState.categories.forEachIndexed { index, item ->
                                        CategoryProgressRow(
                                            index = index + 1,
                                            summary = item
                                        )
                                        if (index < uiState.categories.size - 1) {
                                            Spacer(modifier = Modifier.height(14.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }
    }
}

@Composable
fun TimePill(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgColor = if (isSelected) FinanNavy else Color.Transparent
    val textColor = if (isSelected) Color.White else FinanTextSecondary

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(bgColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = textColor
            )
        )
    }
}

@Composable
fun IncomeVsExpenseBarChart(
    income: Double,
    expense: Double,
    modifier: Modifier = Modifier
) {
    val maxVal = maxOf(income, expense, 1.0)
    val incomeHeightRatio = (income / maxVal).toFloat()
    val expenseHeightRatio = (expense / maxVal).toFloat()

    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .background(Color(0xFFFAFAFA), shape = RoundedCornerShape(12.dp))
                .padding(16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                // Barra de Ingreso
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Box(
                        modifier = Modifier
                            .width(42.dp)
                            .fillMaxHeight(fraction = incomeHeightRatio.coerceIn(0.08f, 1f))
                            .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                            .background(FinanGreen)
                    )
                }

                // Barra de Gasto
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Box(
                        modifier = Modifier
                            .width(42.dp)
                            .fillMaxHeight(fraction = expenseHeightRatio.coerceIn(0.08f, 1f))
                            .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                            .background(FinanExpenseYellow)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "Ingresos",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = FinanGreen
                )
            )
            Text(
                text = "Gastos",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = FinanExpenseYellow
                )
            )
        }
    }
}

@Composable
fun DonutExpenseChart(
    categories: List<CategorySummary>,
    modifier: Modifier = Modifier
) {
    val activeCurrency by CurrencyFormatter.currentCurrency.collectAsState()

    if (categories.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(160.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("Sin datos de gastos en este período", color = FinanTextMuted)
        }
        return
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Lienzo Donut Chart
        Canvas(
            modifier = Modifier
                .size(130.dp)
                .padding(8.dp)
        ) {
            var startAngle = -90f
            val strokeWidth = 24.dp.toPx()

            categories.forEach { cat ->
                val sweep = (cat.percentage / 100f) * 360f
                val (_, color) = getCategoryIconAndColor(cat.category, false)

                drawArc(
                    color = color,
                    startAngle = startAngle,
                    sweepAngle = sweep,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Butt),
                    size = Size(size.width, size.height),
                    topLeft = Offset.Zero
                )
                startAngle += sweep
            }
        }

        // Leyenda
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.take(4).forEach { cat ->
                val (_, color) = getCategoryIconAndColor(cat.category, false)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(color)
                        )
                        Text(
                            text = cat.category,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Medium,
                                color = FinanTextPrimary
                            )
                        )
                    }
                    Text(
                        text = CurrencyFormatter.format(cat.amount, activeCurrency),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = FinanTextPrimary
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryProgressRow(
    index: Int,
    summary: CategorySummary
) {
    val (_, catColor) = getCategoryIconAndColor(summary.category, false)
    val activeCurrency by CurrencyFormatter.currentCurrency.collectAsState()

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF1F5F9)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = index.toString(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = FinanTextSecondary
                        )
                    )
                }
                Text(
                    text = summary.category,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = FinanTextPrimary
                    )
                )
                Text(
                    text = "${summary.percentage.toInt()}%",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = FinanTextMuted
                    )
                )
            }

            Text(
                text = CurrencyFormatter.format(summary.amount, activeCurrency),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = FinanTextPrimary
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = { summary.percentage / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = catColor,
            trackColor = Color(0xFFF1F5F9)
        )
    }
}
