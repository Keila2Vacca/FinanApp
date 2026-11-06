package com.topicos.finanapp.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {

    @Serializable
    data object Dashboard : Screen

    @Serializable
    data class MovementRegister(
        val movementId: Int? = null,
        val initialType: String = "GASTO"
    ) : Screen

    @Serializable
    data object MovementList : Screen

    @Serializable
    data object Statistics : Screen

    @Serializable
    data class MovementDetail(
        val movementId: Int
    ) : Screen

    @Serializable
    data object Settings : Screen
}
