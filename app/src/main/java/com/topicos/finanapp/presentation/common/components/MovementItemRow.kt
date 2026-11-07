package com.topicos.finanapp.presentation.common.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.topicos.finanapp.domain.model.Movement
import com.topicos.finanapp.presentation.common.theme.FinanCardBorder
import com.topicos.finanapp.presentation.common.theme.FinanGreen
import com.topicos.finanapp.presentation.common.theme.FinanTextMuted
import com.topicos.finanapp.presentation.common.theme.FinanTextPrimary
import com.topicos.finanapp.presentation.common.theme.FinanTextSecondary
import com.topicos.finanapp.utils.CurrencyFormatter
import com.topicos.finanapp.utils.DateUtils

@Composable
fun MovementItemRow(
    movement: Movement,
    onClick: () -> Unit,
    onDeleteClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val activeCurrency by CurrencyFormatter.currentCurrency.collectAsState()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, FinanCardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.weight(1f)
            ) {
                CategoryIconBadge(
                    category = movement.category,
                    isIncome = movement.isIncome
                )

                Column {
                    Text(
                        text = movement.description,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = FinanTextPrimary
                        )
                    )
                    Text(
                        text = "${movement.category} · ${DateUtils.formatShortDate(movement.date)}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = FinanTextSecondary,
                            fontSize = 13.sp
                        )
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val amountColor = if (movement.isIncome) FinanGreen else FinanTextPrimary
                Text(
                    text = CurrencyFormatter.formatWithSign(movement.amount, movement.isIncome, activeCurrency),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = amountColor
                    )
                )

                if (onDeleteClick != null) {
                    IconButton(
                        onClick = onDeleteClick,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "Eliminar",
                            tint = FinanTextMuted,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}
