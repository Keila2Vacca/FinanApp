package com.topicos.finanapp.domain.model

data class CategoryItem(
    val id: String,
    val title: String,
    val subtitle: String = ""
)

object MovementCategory {

    // Categorías de Ingresos oficiales especificadas por el usuario
    val incomeCategories: List<CategoryItem> = listOf(
        CategoryItem("Sueldo", "Sueldo", "Salario laboral"),
        CategoryItem("Bonificaciones", "Bonificaciones", "Premios e incentivos"),
        CategoryItem("Comisiones", "Comisiones", "Pagos por ventas o resultados"),
        CategoryItem("Trabajos independientes", "Trabajos independientes", "Servicios y trabajos freelance"),
        CategoryItem("Ventas", "Ventas", "Venta de productos o artículos"),
        CategoryItem("Alquileres", "Alquileres", "Arrendamientos"),
        CategoryItem("Inversiones", "Inversiones", "Intereses y rendimientos"),
        CategoryItem("Pensión y subsidios", "Pensión y subsidios", "Pensiones y ayudas económicas"),
        CategoryItem("Regalos", "Regalos", "Dinero recibido como obsequio"),
        CategoryItem("Otros", "Otros", "Ingresos no clasificados")
    )

    // Categorías de Gastos (se mantienen idénticas)
    val expenseCategories: List<CategoryItem> = listOf(
        CategoryItem("Alimentación", "Alimentación", "Comida y mercado"),
        CategoryItem("Transporte", "Transporte", "Bus, taxi, gasolina"),
        CategoryItem("Vivienda", "Vivienda", "Arriendo y mantenimiento"),
        CategoryItem("Servicios", "Servicios", "Luz, agua, internet"),
        CategoryItem("Salud", "Salud", "Farmacia y consultas"),
        CategoryItem("Educación", "Educación", "Cursos y libros"),
        CategoryItem("Entretenimiento", "Entretenimiento", "Ocio, salidas, juegos"),
        CategoryItem("Compras", "Compras", "Ropa y tecnología"),
        CategoryItem("Otros", "Otros", "Gastos generales")
    )

    fun getCategoriesForType(type: MovementType): List<CategoryItem> {
        return if (type == MovementType.INGRESO) incomeCategories else expenseCategories
    }

    fun getDefaultCategoryForType(type: MovementType): String {
        return if (type == MovementType.INGRESO) "Sueldo" else "Alimentación"
    }

    val allCategories: List<CategoryItem> = (expenseCategories + incomeCategories).distinctBy { it.id }
}
