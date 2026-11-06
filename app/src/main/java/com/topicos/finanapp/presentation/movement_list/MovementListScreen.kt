package com.topicos.finanapp.presentation.movement_list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.topicos.finanapp.domain.model.Movement
import com.topicos.finanapp.navigation.Screen
import com.topicos.finanapp.presentation.common.components.ConfirmDeleteDialog
import com.topicos.finanapp.presentation.common.components.FinanTopBar
import com.topicos.finanapp.presentation.common.components.MovementItemRow
import com.topicos.finanapp.presentation.common.components.SummaryStatCard
import com.topicos.finanapp.presentation.common.theme.FinanBackground
import com.topicos.finanapp.presentation.common.theme.FinanCardBorder
import com.topicos.finanapp.presentation.common.theme.FinanGreen
import com.topicos.finanapp.presentation.common.theme.FinanNavy
import com.topicos.finanapp.presentation.common.theme.FinanTextMuted
import com.topicos.finanapp.presentation.common.theme.FinanTextPrimary
import com.topicos.finanapp.presentation.common.theme.FinanTextSecondary
import com.topicos.finanapp.utils.CurrencyFormatter
import com.topicos.finanapp.utils.DateUtils

@Composable
fun MovementListScreen(
    viewModel: MovementListViewModel,
    onNavigateToRegister: () -> Unit,
    onMovementClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    // Diálogo de confirmación de eliminación
    if (uiState.movementToDelete != null) {
        val mov = uiState.movementToDelete!!
        ConfirmDeleteDialog(
            title = "Eliminar movimiento",
            message = "¿Estás seguro de que deseas eliminar '${mov.description}' por valor de ${CurrencyFormatter.format(mov.amount)}?",
            onConfirm = { viewModel.confirmDeleteMovement() },
            onDismiss = { viewModel.dismissDeleteDialog() }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FinanBackground)
    ) {
        FinanTopBar(subtitle = "Movimientos")

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
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                // Título y botón superior "+ Nuevo"
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Movimientos",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = FinanTextPrimary
                                )
                            )
                            Text(
                                text = "${uiState.movements.size} registros guardados",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = FinanTextSecondary
                                )
                            )
                        }

                        Button(
                            onClick = onNavigateToRegister,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = FinanGreen)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Nuevo",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Fila de 3 tarjetas estadísticas: INGRESOS, GASTOS, BALANCE
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        SummaryStatCard(
                            title = "Ingresos",
                            amountFormatted = CurrencyFormatter.format(uiState.summary.totalIncome),
                            amountColor = FinanGreen,
                            isIncomeArrow = true,
                            modifier = Modifier.weight(1f)
                        )
                        SummaryStatCard(
                            title = "Gastos",
                            amountFormatted = CurrencyFormatter.format(uiState.summary.totalExpense),
                            amountColor = FinanTextPrimary,
                            isIncomeArrow = false,
                            modifier = Modifier.weight(1f)
                        )
                        SummaryStatCard(
                            title = "Balance",
                            amountFormatted = CurrencyFormatter.format(uiState.summary.balance),
                            amountColor = FinanNavy,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Barra de Búsqueda y Filtros de Tipo (Todos / Ingresos / Gastos)
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, FinanCardBorder)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Campo de búsqueda
                            OutlinedTextField(
                                value = uiState.searchQuery,
                                onValueChange = { viewModel.onSearchQueryChanged(it) },
                                placeholder = {
                                    Text(
                                        "Buscar por descripción o categoría",
                                        color = FinanTextMuted,
                                        fontSize = 14.sp
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Buscar",
                                        tint = FinanTextMuted
                                    )
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = FinanGreen,
                                    unfocusedBorderColor = FinanCardBorder
                                ),
                                modifier = Modifier.weight(1f)
                            )

                            // Chips de Filtro
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                FilterChipPill(
                                    title = "Todos",
                                    isSelected = uiState.selectedFilter == "TODOS",
                                    onClick = { viewModel.onFilterChanged("TODOS") }
                                )
                                FilterChipPill(
                                    title = "Ingresos",
                                    isSelected = uiState.selectedFilter == "INGRESOS",
                                    onClick = { viewModel.onFilterChanged("INGRESOS") }
                                )
                                FilterChipPill(
                                    title = "Gastos",
                                    isSelected = uiState.selectedFilter == "GASTOS",
                                    onClick = { viewModel.onFilterChanged("GASTOS") }
                                )
                            }
                        }
                    }
                }

                // Lista de Movimientos Agrupados por Fecha
                if (uiState.filteredMovements.isEmpty()) {
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
                                    .padding(40.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (uiState.searchQuery.isNotBlank())
                                        "No se encontraron movimientos con ese filtro."
                                    else
                                        "No hay movimientos registrados.",
                                    style = MaterialTheme.typography.bodyMedium.copy(color = FinanTextSecondary)
                                )
                            }
                        }
                    }
                } else {
                    val grouped = uiState.filteredMovements.groupBy { DateUtils.formatHeaderDate(it.date) }

                    grouped.forEach { (dateHeader, movementsInGroup) ->
                        item(key = "header_$dateHeader") {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = dateHeader,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = FinanTextSecondary,
                                        letterSpacing = 1.sp
                                    )
                                )
                                Text(
                                    text = "${movementsInGroup.size} mov.",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = FinanTextMuted
                                    )
                                )
                            }
                        }

                        items(movementsInGroup, key = { it.id }) { movement ->
                            MovementItemRow(
                                movement = movement,
                                onClick = { onMovementClick(movement.id) },
                                onDeleteClick = { viewModel.requestDeleteMovement(movement) }
                            )
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
fun FilterChipPill(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgColor = if (isSelected) FinanNavy else Color(0xFFF1F5F9)
    val textColor = if (isSelected) Color.White else FinanTextSecondary

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(bgColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 10.dp),
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
