package com.topicos.finanapp.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {

    private val locale = Locale("es", "CO")

    fun formatDateNumeric(timeMillis: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", locale)
        return sdf.format(Date(timeMillis))
    }

    fun formatShortDate(timeMillis: Long): String {
        val sdf = SimpleDateFormat("d 'de' MMM", locale)
        return sdf.format(Date(timeMillis)).lowercase(locale)
    }

    fun formatHeaderDate(timeMillis: Long): String {
        val nowCal = Calendar.getInstance()
        val itemCal = Calendar.getInstance().apply { timeInMillis = timeMillis }

        val isToday = nowCal.get(Calendar.YEAR) == itemCal.get(Calendar.YEAR) &&
                nowCal.get(Calendar.DAY_OF_YEAR) == itemCal.get(Calendar.DAY_OF_YEAR)

        val isYesterday = nowCal.get(Calendar.YEAR) == itemCal.get(Calendar.YEAR) &&
                nowCal.get(Calendar.DAY_OF_YEAR) - itemCal.get(Calendar.DAY_OF_YEAR) == 1

        return when {
            isToday -> "HOY"
            isYesterday -> "AYER"
            else -> {
                val sdf = SimpleDateFormat("d 'DE' MMMM 'DE' yyyy", locale)
                sdf.format(Date(timeMillis)).uppercase(locale)
            }
        }
    }

    fun formatDashboardDate(timeMillis: Long = System.currentTimeMillis()): String {
        val sdf = SimpleDateFormat("EEEE, d 'De' MMMM", locale)
        val formatted = sdf.format(Date(timeMillis))
        return formatted.split(" ").joinToString(" ") { word ->
            word.replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }
        }
    }
}
