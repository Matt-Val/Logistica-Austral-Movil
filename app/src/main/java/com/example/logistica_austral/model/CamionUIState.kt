package com.example.logistica_austral.model

import android.net.Uri

data class CamionUIState (

    val patente: String = "",
    val marca: String = "",
    val modelo: String = "",
    val annio: String = "",
    val tipo: String = "",
    val capacidad: String = "",
    var disponibilidad: Boolean = true,
    var estado : String = "",
    val descripcion : String = "",
    val traccion : String = "",
    val precio: String = "",
    val imagenUri: Uri? = null // nueva propiedad

)