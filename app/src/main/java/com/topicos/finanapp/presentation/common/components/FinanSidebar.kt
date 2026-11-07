package com.topicos.finanapp.presentation.common.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.topicos.finanapp.navigation.Screen
import com.topicos.finanapp.presentation.common.theme.FinanCardBorder
import com.topicos.finanapp.presentation.common.theme.FinanGreen
import com.topicos.finanapp.presentation.common.theme.FinanNavy
import com.topicos.finanapp.presentation.common.theme.FinanTextMuted
import com.topicos.finanapp.presentation.common.theme.FinanTextSecondary

data class NavItem(
    val title: String,
    val icon: ImageVector,
    val screen: Screen
)

val navItems = listOf(
    NavItem("Inicio", Icons.Default.Home, Screen.Dashboard),
    NavItem("Movimientos", Icons.Default.ReceiptLong, Screen.MovementList),
    NavItem("Registrar", Icons.Default.AddCircleOutline, Screen.MovementRegister()),
    NavItem("Estadísticas", Icons.Default.QueryStats, Screen.Statistics),
    NavItem("Configuración", Icons.Default.Settings, Screen.Settings)
)

@Composable
fun FinanSidebar(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit,
    onNewMovementClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(260.dp)
            .fillMaxHeight()
            .background(Color.White)
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            // App Header con icono y nombre
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(bottom = 32.dp, top = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(FinanNavy),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Wallet,
                        contentDescription = "Logo FinanApp",
                        tint = FinanGreen,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column {
                    Text(
                        text = "FinanApp",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = FinanNavy
                        )
                    )
                    Text(
                        text = "Finanzas personales",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = FinanTextSecondary,
                            fontSize = 12.sp
                        )
                    )
                }
            }

            // Lista de enlaces de navegación
            navItems.forEach { item ->
                val isSelected = when (currentScreen) {
                    is Screen.Dashboard -> item.screen is Screen.Dashboard
                    is Screen.MovementList -> item.screen is Screen.MovementList
                    is Screen.MovementRegister -> item.screen is Screen.MovementRegister
                    is Screen.Statistics -> item.screen is Screen.Statistics
                    is Screen.Settings -> item.screen is Screen.Settings
                    is Screen.MovementDetail -> false
                }

                val bgColor = if (isSelected) FinanNavy else Color.Transparent
                val contentColor = if (isSelected) Color.White else FinanTextSecondary

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(bgColor)
                        .clickable { onNavigate(item.screen) }
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        tint = contentColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = contentColor
                        )
                    )
                }
            }
        }

        // Widget inferior: "Registra cada movimiento"
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
            border = BorderStroke(1.dp, FinanCardBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Registra cada movimiento",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = FinanNavy
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Mantén tus cuentas al día para ver tu balance real.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = FinanTextMuted,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onNewMovementClick,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FinanGreen),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Nuevo movimiento",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
            }
        }
    }
}

