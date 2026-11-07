package com.topicos.finanapp.utils

import com.topicos.finanapp.domain.model.AppCurrency
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object CurrencyFormatter {

    private val symbols = DecimalFormatSymbols(Locale("es", "CO")).apply {
        groupingSeparator = '.'
        decimalSeparator = ','
    }

    private val formatter = DecimalFormat("#,##0", symbols)

    private val _currentCurrency = MutableStateFlow(AppCurrency.defaultCurrency)
    val currentCurrency: StateFlow<AppCurrency> = _currentCurrency.asStateFlow()

    fun setCurrency(currency: AppCurrency) {
        _currentCurrency.value = currency
    }

    fun getCurrency(): AppCurrency = _currentCurrency.value

    fun getCurrencySymbol(): String = _currentCurrency.value.symbol

    fun getCurrencyCode(): String = _currentCurrency.value.code

    fun format(amount: Double, currency: AppCurrency = _currentCurrency.value): String {
        val numberString = formatter.format(amount)
        return if (currency.symbolPrefix) {
            "${currency.symbol} $numberString"
        } else {
            "$numberString ${currency.symbol}"
        }
    }

    fun formatWithSign(amount: Double, isIncome: Boolean, currency: AppCurrency = _currentCurrency.value): String {
        val sign = if (isIncome) "+" else "-"
        val numberString = formatter.format(amount)
        return if (currency.symbolPrefix) {
            "$sign${currency.symbol} $numberString"
        } else {
            "$sign$numberString ${currency.symbol}"
        }
    }

    fun formatCompact(amount: Double, currency: AppCurrency = _currentCurrency.value): String {
        val formatted = when {
            amount >= 1_000_000 -> "${formatter.format(amount / 1_000_000)}M"
            amount >= 1_000 -> "${formatter.format(amount / 1_000)}K"
            else -> formatter.format(amount)
        }
        return if (currency.symbolPrefix) {
            "${currency.symbol} $formatted"
        } else {
            "$formatted ${currency.symbol}"
        }
    }

    /**
     * Formatea una cadena de dígitos puros agregando puntos de separación de miles
     * para facilitar la lectura del usuario mientras escribe (ej. "2500000" -> "2.500.000").
     */
    fun formatInputWithDots(digitsOnly: String): String {
        val clean = digitsOnly.filter { it.isDigit() }
        if (clean.isEmpty()) return ""
        val parsed = clean.toLongOrNull() ?: return clean
        return formatter.format(parsed)
    }
}
