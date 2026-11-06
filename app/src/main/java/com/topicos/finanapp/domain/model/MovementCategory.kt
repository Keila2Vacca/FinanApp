package com.topicos.finanapp.domain.model

enum class MovementCategory(val id: String, val title: String) {
    ALIMENTACION("Alimentación", "Alimentación"),
    TRANSPORTE("Transporte", "Transporte"),
    VIVIENDA("Vivienda", "Vivienda"),
    SERVICIOS("Servicios", "Servicios"),
    SALUD("Salud", "Salud"),
    EDUCACION("Educación", "Educación"),
    ENTRETENIMIENTO("Entretenimiento", "Entretenimiento"),
    COMPRAS("Compras", "Compras"),
    OTROS("Otros", "Otros");

    companion object {
        fun fromString(value: String): MovementCategory {
            return entries.firstOrNull { it.id.equals(value, ignoreCase = true) } ?: OTROS
        }

        val allCategories = entries.toList()
    }
}
