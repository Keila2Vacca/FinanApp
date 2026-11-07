package com.topicos.finanapp

import android.app.Application
import android.content.Context
import com.topicos.finanapp.data.local.MovementDatabase
import com.topicos.finanapp.data.repository.MovementRepositoryImpl
import com.topicos.finanapp.domain.model.AppCurrency
import com.topicos.finanapp.domain.repository.MovementRepository
import com.topicos.finanapp.utils.CurrencyFormatter

class FinanApplication : Application() {

    lateinit var database: MovementDatabase
        private set

    lateinit var repository: MovementRepository
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        database = MovementDatabase.getInstance(this)
        repository = MovementRepositoryImpl(database.movementDao())

        // Cargar moneda guardada en preferencias del usuario
        try {
            val prefs = getSharedPreferences("finanapp_prefs", Context.MODE_PRIVATE)
            val savedCurrencyCode = prefs.getString("selected_currency", AppCurrency.defaultCurrency.code)
                ?: AppCurrency.defaultCurrency.code
            CurrencyFormatter.setCurrency(AppCurrency.fromCode(savedCurrencyCode))
        } catch (_: Exception) {}
    }

    companion object {
        lateinit var instance: FinanApplication
            private set
    }
}
