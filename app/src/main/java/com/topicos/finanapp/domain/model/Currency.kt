package com.topicos.finanapp.domain.model

enum class AppCurrency(
    val code: String,
    val displayName: String,
    val symbol: String,
    val symbolPrefix: Boolean = true
) {
    COP(
        code = "COP",
        displayName = "Peso colombiano (COP)",
        symbol = "$"
    ),
    USD(
        code = "USD",
        displayName = "Dólar estadounidense (USD)",
        symbol = "US$"
    ),
    EUR(
        code = "EUR",
        displayName = "Euro (EUR)",
        symbol = "€",
        symbolPrefix = false
    ),
    MXN(
        code = "MXN",
        displayName = "Peso mexicano (MXN)",
        symbol = "$ MXN"
    ),
    CNY(
        code = "CNY",
        displayName = "Yuan chino (CNY)",
        symbol = "¥"
    ),
    GBP(
        code = "GBP",
        displayName = "Libra esterlina (GBP)",
        symbol = "£"
    ),
    PEN(
        code = "PEN",
        displayName = "Sol peruano (PEN)",
        symbol = "S/"
    ),
    BRL(
        code = "BRL",
        displayName = "Real brasileño (BRL)",
        symbol = "R$"
    ),
    JPY(
        code = "JPY",
        displayName = "Yen japonés (JPY)",
        symbol = "¥ JPY"
    );

    companion object {
        val defaultCurrency = COP

        fun fromCode(code: String): AppCurrency {
            return entries.firstOrNull { it.code.equalsIgnoreCase(code) } ?: defaultCurrency
        }
    }
}

private fun String.equalsIgnoreCase(other: String): Boolean {
    return this.equals(other, ignoreCase = true)
}
