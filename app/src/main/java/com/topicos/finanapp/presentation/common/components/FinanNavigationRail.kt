package com.topicos.finanapp.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.topicos.finanapp.navigation.Screen
import com.topicos.finanapp.presentation.common.theme.FinanGreen
import com.topicos.finanapp.presentation.common.theme.FinanNavy
import com.topicos.finanapp.presentation.common.theme.FinanTextSecondary

@Composable
fun FinanNavigationRail(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit,
    onNewMovementClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationRail(
        modifier = modifier.fillMaxHeight(),
        containerColor = Color.White,
        contentColor = FinanTextSecondary,
        header = {
            Box(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(FinanNavy),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Wallet,
                    contentDescription = "Logo",
                    tint = FinanGreen,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    ) {
        navItems.forEach { item ->
            val isSelected = when (currentScreen) {
                is Screen.Dashboard -> item.screen is Screen.Dashboard
                is Screen.MovementList -> item.screen is Screen.MovementList
                is Screen.MovementRegister -> item.screen is Screen.MovementRegister
                is Screen.Statistics -> item.screen is Screen.Statistics
                is Screen.Settings -> item.screen is Screen.Settings
                is Screen.MovementDetail -> false
            }

            NavigationRailItem(
                selected = isSelected,
                onClick = { onNavigate(item.screen) },
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = FinanNavy,
                    indicatorColor = FinanNavy,
                    unselectedIconColor = FinanTextSecondary,
                    unselectedTextColor = FinanTextSecondary
                )
            )
        }

        FloatingActionButton(
            onClick = onNewMovementClick,
            containerColor = FinanGreen,
            contentColor = Color.White,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Nuevo")
        }
    }
}

