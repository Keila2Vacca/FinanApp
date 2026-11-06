package com.topicos.finanapp.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object CurrencyFormatter {

    private val symbols = DecimalFormatSymbols(Locale("es", "CO")).apply {
        groupingSeparator = '.'
        decimalSeparator = ','
    }

    private val formatter = DecimalFormat("#,##0", symbols)

    fun format(amount: Double): String {
        return "$ ${formatter.format(amount)}"
    }

    fun formatWithSign(amount: Double, isIncome: Boolean): String {
        val sign = if (isIncome) "+$" else "-$"
        return "$sign ${formatter.format(amount)}"
    }

    fun formatCompact(amount: Double): String {
        return when {
            amount >= 1_000_000 -> "${formatter.format(amount / 1_000_000)}M"
            amount >= 1_000 -> "${formatter.format(amount / 1_000)}K"
            else -> formatter.format(amount)
        }
    }
}
