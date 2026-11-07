package com.topicos.finanapp.presentation.movement_register

import android.app.DatePickerDialog
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.topicos.finanapp.domain.model.MovementCategory
import com.topicos.finanapp.domain.model.MovementType
import com.topicos.finanapp.presentation.common.components.FinanTopBar
import com.topicos.finanapp.presentation.common.components.getCategoryIconAndColor
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
import java.util.Calendar

@Composable
fun MovementRegisterScreen(
    viewModel: MovementRegisterViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val activeCurrency by CurrencyFormatter.currentCurrency.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState.isSavedSuccess) {
        if (uiState.isSavedSuccess) {
            onBack()
        }
    }

    val calendar = Calendar.getInstance().apply { timeInMillis = uiState.dateMillis }
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selected = Calendar.getInstance().apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
            }
            viewModel.onDateChanged(selected.timeInMillis)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // Valor formateado con puntos de miles para mostrar visualmente al usuario
    val formattedDisplayValue = remember(uiState.amountInput) {
        CurrencyFormatter.formatInputWithDots(uiState.amountInput)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FinanBackground)
    ) {
        FinanTopBar(subtitle = if (uiState.isEditing) "Editar movimiento" else "Registrar movimiento")

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Cabecera con botón atrás y descripción
            item {
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
                            text = if (uiState.isEditing) "Editar movimiento" else "Registrar movimiento",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = FinanTextPrimary
                            )
                        )
                        Text(
                            text = "Completa los datos para actualizar tu balance automáticamente.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = FinanTextSecondary
                            )
                        )
                    }
                }
            }

            // Selector Segmentado de Tipo: [◉ Gasto] [○ Ingreso]
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, FinanCardBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TypeSelectorButton(
                            title = "Gasto",
                            isSelected = uiState.type == MovementType.GASTO,
                            selectedBgColor = FinanNavy,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.onTypeChanged(MovementType.GASTO) }
                        )
                        TypeSelectorButton(
                            title = "Ingreso",
                            isSelected = uiState.type == MovementType.INGRESO,
                            selectedBgColor = FinanGreen,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.onTypeChanged(MovementType.INGRESO) }
                        )
                    }
                }
            }

            // Tarjeta de Valor del Movimiento (con puntos de miles automáticos)
            item {
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
                                text = "VALOR DEL MOVIMIENTO",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = FinanTextSecondary,
                                    letterSpacing = 1.sp
                                )
                            )

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFFF1F5F9))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "${uiState.type.value} (${activeCurrency.code})",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (uiState.type == MovementType.INGRESO) FinanGreen else FinanNavy
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = activeCurrency.symbol,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = FinanTextPrimary,
                                    fontSize = 32.sp
                                )
                            )
                            OutlinedTextField(
                                value = formattedDisplayValue,
                                onValueChange = { newValue ->
                                    // Filtra los dígitos y actualiza el estado numérico puro
                                    val digits = newValue.filter { it.isDigit() }
                                    viewModel.onAmountChanged(digits)
                                },
                                placeholder = {
                                    Text(
                                        "0",
                                        style = MaterialTheme.typography.headlineLarge.copy(
                                            color = FinanTextMuted,
                                            fontSize = 32.sp
                                        )
                                    )
                                },
                                textStyle = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = FinanTextPrimary,
                                    fontSize = 32.sp
                                ),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedBorderColor = Color.Transparent,
                                    focusedBorderColor = Color.Transparent
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Text(
                            text = "Escribe solo números. Los puntos de separación de miles se agregan automáticamente.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = FinanTextMuted
                            )
                        )
                    }
                }
            }

            // Campo de Descripción
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, FinanCardBorder)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Descripción",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = FinanTextPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = uiState.description,
                            onValueChange = { viewModel.onDescriptionChanged(it) },
                            placeholder = {
                                Text(if (uiState.type == MovementType.INGRESO) "Ej. Sueldo mensual, bono" else "Ej. Mercado de la semana, arriendo")
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = FinanGreen,
                                unfocusedBorderColor = FinanCardBorder
                            )
                        )
                    }
                }
            }

            // Selector de Categoría (Diferenciado según Ingreso o Gasto)
            item {
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
                                text = "Categoría",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = FinanTextPrimary
                                )
                            )

                            Text(
                                text = if (uiState.type == MovementType.INGRESO) "Categorías de Ingreso" else "Categorías de Gasto",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (uiState.type == MovementType.INGRESO) FinanGreen else FinanNavy,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Grilla de categorías correspondiente al tipo seleccionado
                        val currentCategories = MovementCategory.getCategoriesForType(uiState.type)
                        val chunked = currentCategories.chunked(2)

                        chunked.forEach { rowCategories ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                rowCategories.forEach { category ->
                                    val isSelected = uiState.category.equals(category.id, ignoreCase = true)
                                    CategoryGridButton(
                                        categoryName = category.title,
                                        subtitle = category.subtitle,
                                        isSelected = isSelected,
                                        isIncome = uiState.type == MovementType.INGRESO,
                                        modifier = Modifier.weight(1f),
                                        onClick = { viewModel.onCategorySelected(category.id) }
                                    )
                                }
                                if (rowCategories.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }

            // Fila de Fecha y Nota (opcional)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Selector de Fecha
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { datePickerDialog.show() },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, FinanCardBorder)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Fecha",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = FinanTextPrimary
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = DateUtils.formatDateNumeric(uiState.dateMillis),
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = FinanTextPrimary
                                    )
                                )
                                Icon(
                                    imageVector = Icons.Default.CalendarToday,
                                    contentDescription = "Calendario",
                                    tint = FinanTextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    // Nota opcional
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, FinanCardBorder)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Nota (opcional)",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = FinanTextPrimary
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = uiState.note,
                                onValueChange = { viewModel.onNoteChanged(it) },
                                placeholder = { Text("Detalle del movimiento", fontSize = 13.sp) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = FinanGreen,
                                    unfocusedBorderColor = Color.Transparent
                                )
                            )
                        }
                    }
                }
            }

            // Mensaje de error si la validación falla
            if (uiState.errorMessage != null) {
                item {
                    Text(
                        text = uiState.errorMessage ?: "",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = FinanExpense,
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }

            // Botones Cancelar y Guardar movimiento
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedButton(
                        onClick = onBack,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, FinanCardBorder),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = FinanTextPrimary),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                    ) {
                        Text(
                            text = "Cancelar",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }

                    Button(
                        onClick = { viewModel.saveMovement() },
                        enabled = !uiState.isSaving,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = FinanGreen),
                        modifier = Modifier
                            .weight(1.5f)
                            .height(50.dp)
                    ) {
                        if (uiState.isSaving) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        } else {
                            Text(
                                text = if (uiState.isEditing) "Actualizar movimiento" else "Guardar movimiento",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
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

@Composable
fun TypeSelectorButton(
    title: String,
    isSelected: Boolean,
    selectedBgColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val bgColor = if (isSelected) selectedBgColor else Color.Transparent
    val contentColor = if (isSelected) Color.White else FinanTextSecondary

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(bgColor)
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(CircleShape)
                .background(if (isSelected) Color.White else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(selectedBgColor)
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = contentColor
            )
        )
    }
}

@Composable
fun CategoryGridButton(
    categoryName: String,
    subtitle: String,
    isSelected: Boolean,
    isIncome: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val (icon, tint) = getCategoryIconAndColor(categoryName, isIncome)
    val bgColor = if (isSelected) {
        if (isIncome) FinanGreen.copy(alpha = 0.12f) else FinanNavy.copy(alpha = 0.08f)
    } else Color.White

    val activeColor = if (isIncome) FinanGreen else FinanNavy
    val borderStroke = if (isSelected) BorderStroke(1.5.dp, activeColor) else BorderStroke(1.dp, FinanCardBorder)

    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        border = borderStroke
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isSelected) activeColor else tint.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isSelected) Color.White else tint,
                    modifier = Modifier.size(18.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = categoryName,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                        color = if (isSelected) activeColor else FinanTextPrimary,
                        fontSize = 12.sp
                    ),
                    maxLines = 1
                )
                if (subtitle.isNotBlank()) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = FinanTextMuted,
                            fontSize = 10.sp
                        ),
                        maxLines = 1
                    )
                }
            }
        }
    }
}
