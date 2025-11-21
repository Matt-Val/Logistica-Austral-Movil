package com.example.logistica_austral.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Camion::class, Usuario::class], version = 3, exportSchema = false)
@TypeConverters(UriConverters::class)
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
                )
                    .fallbackToDestructiveMigration() // reinicia si no hay migraciones D:
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
