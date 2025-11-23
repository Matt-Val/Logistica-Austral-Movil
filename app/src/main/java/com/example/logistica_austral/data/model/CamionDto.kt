package com.example.logistica_austral.data.model

/**
 * DTO utilizado para operaciones remotas la creación de camion.
 * No incluye id ni imagenUri porque el backend los asigna.
 */
data class CamionDto(
    val patente: String,
    val marca: String,
    val modelo: String,
    val annio: Int,
    val tipo: String,
    val capacidad: Int,
    val disponibilidad: Boolean,
    val estado: String,
    val descripcion: String,
    val traccion: String,
    val precio: Int
)
