package com.example.logistica_austral.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Camion::class, Usuario::class], version = 2, exportSchema = false)
// Crea la base de datos con las tablas Camion y Usuario

abstract class AppDatabase : RoomDatabase() {
    abstract fun camionDao(): CamionDao
    abstract fun usuarioDao(): UsuarioDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "logistica_austral_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
