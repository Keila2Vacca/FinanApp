package com.topicos.finanapp.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Redeem
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.TrendingUp
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
    val cat = category.trim().lowercase()

    if (isIncome) {
        return when {
            cat.contains("sueldo") || cat.contains("salario") -> Icons.Default.Payments to Color(0xFF10B981)
            cat.contains("bonificaci") || cat.contains("incentivo") -> Icons.Default.Redeem to Color(0xFF059669)
            cat.contains("comisi") || cat.contains("resultado") -> Icons.Default.TrendingUp to Color(0xFF14B8A6)
            cat.contains("independiente") || cat.contains("freelance") -> Icons.Default.Laptop to Color(0xFF0284C7)
            cat.contains("venta") -> Icons.Default.Storefront to Color(0xFF6366F1)
            cat.contains("alquiler") || cat.contains("arrendamiento") -> Icons.Default.Apartment to Color(0xFF8B5CF6)
            cat.contains("inversi") || cat.contains("rendimiento") -> Icons.Default.ShowChart to Color(0xFF10B981)
            cat.contains("pensi") || cat.contains("subsidio") -> Icons.Default.Security to Color(0xFFF59E0B)
            cat.contains("regalo") || cat.contains("obsequio") -> Icons.Default.CardGiftcard to Color(0xFFEC4899)
            else -> Icons.Default.AccountBalanceWallet to Color(0xFF10B981)
        }
    }

    return when {
        cat.contains("alimentaci") -> Icons.Default.Restaurant to Color(0xFFF59E0B)
        cat.contains("transporte") -> Icons.Default.DirectionsBus to Color(0xFF3B82F6)
        cat.contains("vivienda") -> Icons.Default.Home to Color(0xFF10B981)
        cat.contains("servicio") -> Icons.Default.Build to Color(0xFF6366F1)
        cat.contains("salud") -> Icons.Default.Favorite to Color(0xFFEF4444)
        cat.contains("educaci") -> Icons.Default.School to Color(0xFF8B5CF6)
        cat.contains("entretenimiento") -> Icons.Default.SportsEsports to Color(0xFFEC4899)
        cat.contains("compra") -> Icons.Default.ShoppingBag to Color(0xFF14B8A6)
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
