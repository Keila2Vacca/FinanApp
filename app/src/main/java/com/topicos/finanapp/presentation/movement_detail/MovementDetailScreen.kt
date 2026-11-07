package com.topicos.finanapp.presentation.movement_detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.topicos.finanapp.presentation.common.components.CategoryIconBadge
import com.topicos.finanapp.presentation.common.components.ConfirmDeleteDialog
import com.topicos.finanapp.presentation.common.components.FinanTopBar
import com.topicos.finanapp.presentation.common.theme.FinanBackground
import com.topicos.finanapp.presentation.common.theme.FinanCardBorder
import com.topicos.finanapp.presentation.common.theme.FinanExpense
import com.topicos.finanapp.presentation.common.theme.FinanGreen
import com.topicos.finanapp.presentation.common.theme.FinanNavy
import com.topicos.finanapp.presentation.common.theme.FinanTextMuted
import com.topicos.finanapp.presentation.common.theme.FinanTextPrimary
import com.topicos.finanapp.presentation.common.theme.FinanTextSecondary
import com.topicos.finanapp.utils.CurrencyFormatter
import com.topicos.finanapp.utils.DateUtils

@Composable
fun MovementDetailScreen(
    viewModel: MovementDetailViewModel,
    onBack: () -> Unit,
    onEditClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val activeCurrency by CurrencyFormatter.currentCurrency.collectAsState()

    LaunchedEffect(uiState.isDeleted) {
        if (uiState.isDeleted) {
            onBack()
        }
    }

    if (uiState.showDeleteDialog && uiState.movement != null) {
        ConfirmDeleteDialog(
            title = "Eliminar movimiento",
            message = "¿Deseas eliminar definitivamente este registro?",
            onConfirm = { viewModel.confirmDelete() },
            onDismiss = { viewModel.dismissDeleteDialog() }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FinanBackground)
    ) {
        FinanTopBar(subtitle = "Detalle del movimiento")

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = FinanGreen)
            }
        } else if (uiState.movement != null) {
            val movement = uiState.movement!!

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Cabecera con botón volver
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás",
                            tint = FinanNavy
                        )
                    }

                    Column {
                        Text(
                            text = "Detalle del registro",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = FinanTextPrimary
                            )
                        )
                        Text(
                            text = "Consulta y administra este movimiento financiero.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = FinanTextSecondary
                            )
                        )
                    }
                }

                // Tarjeta Principal de Información
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, FinanCardBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        // Badge de Categoría y Valor
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                CategoryIconBadge(
                                    category = movement.category,
                                    isIncome = movement.isIncome,
                                    size = 50.dp,
                                    iconSize = 26.dp
                                )

                                Column {
                                    Text(
                                        text = movement.description,
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = FinanTextPrimary
                                        )
                                    )
                                    Text(
                                        text = movement.category,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = FinanTextSecondary
                                        )
                                    )
                                }
                            }

                            val amountColor = if (movement.isIncome) FinanGreen else FinanNavy
                            Text(
                                text = CurrencyFormatter.formatWithSign(movement.amount, movement.isIncome, activeCurrency),
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = amountColor
                                )
                            )
                        }

                        HorizontalDivider(color = FinanCardBorder, thickness = 1.dp)

                        // Atributos Detallados
                        DetailRow(label = "Tipo de movimiento", value = movement.type.displayName)
                        DetailRow(label = "Categoría", value = movement.category)
                        DetailRow(label = "Fecha de registro", value = DateUtils.formatDateNumeric(movement.date))

                        if (movement.note.isNotBlank()) {
                            DetailRow(label = "Nota adicional", value = movement.note)
                        }
                    }
                }

                // Botones de Acción: Editar y Eliminar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = { onEditClick(movement.id) },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = FinanNavy),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Editar movimiento",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }

                    OutlinedButton(
                        onClick = { viewModel.showDeleteDialog() },
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, FinanExpense.copy(alpha = 0.5f)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = FinanExpense),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = FinanExpense,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Eliminar",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = FinanExpense
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = FinanTextSecondary
            )
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.SemiBold,
                color = FinanTextPrimary
            )
        )
    }
}

