package com.topicos.finanapp

import android.app.Application
import com.topicos.finanapp.data.local.MovementDatabase
import com.topicos.finanapp.data.repository.MovementRepositoryImpl
import com.topicos.finanapp.domain.repository.MovementRepository

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
    }

    companion object {
        lateinit var instance: FinanApplication
            private set
    }
}
