package com.topicos.finanapp.domain.model

enum class MovementType(val value: String, val displayName: String) {
    INGRESO("INGRESO", "Ingreso"),
    GASTO("GASTO", "Gasto");

    companion object {
        fun fromString(value: String): MovementType {
            return entries.firstOrNull { it.value.equals(value, ignoreCase = true) } ?: GASTO
        }
    }
}

