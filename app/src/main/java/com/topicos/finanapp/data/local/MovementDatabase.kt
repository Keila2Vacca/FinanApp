package com.topicos.finanapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.topicos.finanapp.data.local.dao.MovementDao
import com.topicos.finanapp.data.local.entity.MovementEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [MovementEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MovementDatabase : RoomDatabase() {

    abstract fun movementDao(): MovementDao

    companion object {
        @Volatile
        private var INSTANCE: MovementDatabase? = null

        fun getInstance(context: Context): MovementDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovementDatabase::class.java,
                    "finanapp_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Pre-poblar datos de ejemplo iniciales requeridos por el examen
                            CoroutineScope(Dispatchers.IO).launch {
                                INSTANCE?.let { database ->
                                    populateInitialData(database.movementDao())
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        suspend fun populateInitialData(dao: MovementDao) {
            val now = System.currentTimeMillis()
            val oneDay = 86400000L
            val initialList = listOf(
                MovementEntity(
                    id = 1,
                    description = "Salario mensual",
                    amount = 2500000.0,
                    type = "INGRESO",
                    category = "Sueldo",
                    date = now - (oneDay * 4), // 2 de noviembre
                    note = "Pago nómina mes actual"
                ),
                MovementEntity(
                    id = 2,
                    description = "Arriendo apartamento",
                    amount = 3500000.0 / 10.0, // 350.000
                    type = "GASTO",
                    category = "Vivienda",
                    date = now - (oneDay * 3), // 3 de noviembre
                    note = "Pago arriendo mensual"
                ),
                MovementEntity(
                    id = 3,
                    description = "Mercado de la semana",
                    amount = 150000.0,
                    type = "GASTO",
                    category = "Alimentación",
                    date = now - (oneDay * 1), // Ayer
                    note = "Compras supermercado"
                )
            )
            dao.insertAll(initialList)
        }
    }
}

