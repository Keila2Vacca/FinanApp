package com.topicos.finanapp.presentation.common.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.topicos.finanapp.navigation.Screen
import com.topicos.finanapp.presentation.common.theme.FinanNavy
import com.topicos.finanapp.presentation.common.theme.FinanTextSecondary

@Composable
fun FinanBottomBar(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = Color.White,
        tonalElevation = 8.dp
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

            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.screen) },
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = FinanNavy,
                    indicatorColor = FinanNavy,
                    unselectedIconColor = FinanTextSecondary,
                    unselectedTextColor = FinanTextSecondary
                )
            )
        }
    }
}
