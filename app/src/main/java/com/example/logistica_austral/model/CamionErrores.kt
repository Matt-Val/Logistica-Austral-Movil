package com.example.logistica_austral.model

data class CamionErrores(
    val esErrorPatente: Boolean = false,
    val esErrorMarca: Boolean = false,
    val esErrorModelo: Boolean = false,
    val esErrorAnnio: Boolean = false,
    val esErrorTipo: Boolean = false,
    val esErrorCapacidad: Boolean = false,
    val esErrorEstado: Boolean = false,
    val esErrorDescripcion: Boolean = false,
    val esErrorTraccion: Boolean = false,

    )