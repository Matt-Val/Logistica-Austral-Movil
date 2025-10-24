package com.example.logistica_austral.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Camion::class], version=1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun camionDao(): CamionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context):AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
