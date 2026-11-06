package com.topicos.finanapp.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun getCategoryIconAndColor(category: String, isIncome: Boolean): Pair<ImageVector, Color> {
    if (isIncome) {
        return Icons.Default.AccountBalanceWallet to Color(0xFF10B981)
    }
    return when (category.lowercase()) {
        "alimentación", "alimentacion" -> Icons.Default.Restaurant to Color(0xFFF59E0B)
        "transporte" -> Icons.Default.DirectionsBus to Color(0xFF3B82F6)
        "vivienda" -> Icons.Default.Home to Color(0xFF10B981)
        "servicios" -> Icons.Default.Build to Color(0xFF6366F1)
        "salud" -> Icons.Default.Favorite to Color(0xFFEF4444)
        "educación", "educacion" -> Icons.Default.School to Color(0xFF8B5CF6)
        "entretenimiento" -> Icons.Default.SportsEsports to Color(0xFFEC4899)
        "compras" -> Icons.Default.ShoppingBag to Color(0xFF14B8A6)
        else -> Icons.Default.MoreHoriz to Color(0xFF64748B)
    }
}

@Composable
fun CategoryIconBadge(
    category: String,
    isIncome: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 44.dp,
    iconSize: Dp = 22.dp
) {
    val (icon, tint) = getCategoryIconAndColor(category, isIncome)
    val bgColor = tint.copy(alpha = 0.12f)

    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = category,
            tint = tint,
            modifier = Modifier.size(iconSize)
        )
    }
}
