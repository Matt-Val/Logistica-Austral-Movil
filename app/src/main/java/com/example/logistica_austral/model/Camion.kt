package com.example.logistica_austral.model
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "camiones")
data class Camion(

    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val patente: String,
    val marca: String,
    val modelo: String,
    val annio: Int,
    val tipo: String,
    val capacidad: Int,
    var disponibilidad: Boolean,
    var estado: String, // Cambia con el tiempo
    val descripcion: String,
    val traccion: String
)