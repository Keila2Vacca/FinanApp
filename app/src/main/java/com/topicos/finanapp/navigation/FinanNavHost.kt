package com.topicos.finanapp.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.topicos.finanapp.FinanApplication
import com.topicos.finanapp.presentation.common.components.FinanBottomBar
import com.topicos.finanapp.presentation.common.components.FinanNavigationRail
import com.topicos.finanapp.presentation.common.components.FinanSidebar
import com.topicos.finanapp.presentation.common.theme.FinanBackground
import com.topicos.finanapp.presentation.dashboard.DashboardScreen
import com.topicos.finanapp.presentation.dashboard.DashboardViewModel
import com.topicos.finanapp.presentation.movement_detail.MovementDetailScreen
import com.topicos.finanapp.presentation.movement_detail.MovementDetailViewModel
import com.topicos.finanapp.presentation.movement_list.MovementListScreen
import com.topicos.finanapp.presentation.movement_list.MovementListViewModel
import com.topicos.finanapp.presentation.movement_register.MovementRegisterScreen
import com.topicos.finanapp.presentation.movement_register.MovementRegisterViewModel
import com.topicos.finanapp.presentation.settings.SettingsScreen
import com.topicos.finanapp.presentation.settings.SettingsViewModel
import com.topicos.finanapp.presentation.statistics.StatisticsScreen
import com.topicos.finanapp.presentation.statistics.StatisticsViewModel
import com.topicos.finanapp.utils.CurrencyFormatter

@Composable
fun FinanNavHost(
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier
) {
    val repository = FinanApplication.instance.repository
    val activeCurrency by CurrencyFormatter.currentCurrency.collectAsState()
    val backStack = remember { mutableStateListOf<Screen>(Screen.Dashboard) }
    val currentScreen = backStack.lastOrNull() ?: Screen.Dashboard

    // Manejador del botón físico atrás del sistema
    BackHandler(enabled = backStack.size > 1) {
        backStack.removeLastOrNull()
    }

    val onNavigate: (Screen) -> Unit = { screen ->
        if (screen == Screen.Dashboard) {
            backStack.clear()
            backStack.add(Screen.Dashboard)
        } else {
            // Evitar duplicar la misma pantalla consecutivamente
            if (backStack.lastOrNull() != screen) {
                backStack.add(screen)
            }
        }
    }

    val onBack: () -> Unit = {
        if (backStack.size > 1) {
            backStack.removeLastOrNull()
        }
    }

    val isCompact = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact
    val isMedium = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Medium
    val isExpanded = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded

    // Disposición adaptativa según Material 3 WindowSizeClass
    if (isCompact) {
        // Celulares: Barra inferior de navegación
        Scaffold(
            bottomBar = {
                FinanBottomBar(
                    currentScreen = currentScreen,
                    onNavigate = onNavigate
                )
            },
            modifier = modifier.fillMaxSize()
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(FinanBackground)
            ) {
                ScreenContent(
                    screen = currentScreen,
                    repository = repository,
                    onNavigate = onNavigate,
                    onBack = onBack
                )
            }
        }
    } else {
        // Pantallas medianas y grandes (Tablets / Mockups oficiales): Barra lateral
        Row(
            modifier = modifier
                .fillMaxSize()
                .background(FinanBackground)
        ) {
            if (isMedium) {
                FinanNavigationRail(
                    currentScreen = currentScreen,
                    onNavigate = onNavigate,
                    onNewMovementClick = { onNavigate(Screen.MovementRegister()) }
                )
            } else {
                FinanSidebar(
                    currentScreen = currentScreen,
                    onNavigate = onNavigate,
                    onNewMovementClick = { onNavigate(Screen.MovementRegister()) }
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                ScreenContent(
                    screen = currentScreen,
                    repository = repository,
                    onNavigate = onNavigate,
                    onBack = onBack
                )
            }
        }
    }
}

@Composable
private fun ScreenContent(
    screen: Screen,
    repository: com.topicos.finanapp.domain.repository.MovementRepository,
    onNavigate: (Screen) -> Unit,
    onBack: () -> Unit
) {
    when (screen) {
        is Screen.Dashboard -> {
            val viewModel: DashboardViewModel = viewModel { DashboardViewModel(repository) }
            DashboardScreen(
                viewModel = viewModel,
                onNavigate = onNavigate,
                onMovementClick = { id -> onNavigate(Screen.MovementDetail(id)) }
            )
        }

        is Screen.MovementList -> {
            val viewModel: MovementListViewModel = viewModel { MovementListViewModel(repository) }
            MovementListScreen(
                viewModel = viewModel,
                onNavigateToRegister = { onNavigate(Screen.MovementRegister()) },
                onMovementClick = { id -> onNavigate(Screen.MovementDetail(id)) }
            )
        }

        is Screen.MovementRegister -> {
            val viewModel: MovementRegisterViewModel = viewModel(key = "register_${screen.movementId}_${screen.initialType}") {
                MovementRegisterViewModel(
                    repository = repository,
                    movementId = screen.movementId,
                    initialType = screen.initialType
                )
            }
            MovementRegisterScreen(
                viewModel = viewModel,
                onBack = onBack
            )
        }

        is Screen.MovementDetail -> {
            val viewModel: MovementDetailViewModel = viewModel(key = "detail_${screen.movementId}") {
                MovementDetailViewModel(
                    repository = repository,
                    movementId = screen.movementId
                )
            }
            MovementDetailScreen(
                viewModel = viewModel,
                onBack = onBack,
                onEditClick = { id -> onNavigate(Screen.MovementRegister(movementId = id)) }
            )
        }

        is Screen.Statistics -> {
            val viewModel: StatisticsViewModel = viewModel { StatisticsViewModel(repository) }
            StatisticsScreen(
                viewModel = viewModel
            )
        }

        is Screen.Settings -> {
            val viewModel: SettingsViewModel = viewModel { SettingsViewModel(repository) }
            SettingsScreen(
                viewModel = viewModel
            )
        }
    }
}

