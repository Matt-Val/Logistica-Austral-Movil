package com.example.logistica_austral.model

import android.net.Uri
import androidx.room.TypeConverter

// Camion tiene imagenUri como Uri?, Segun, Room necesita el TypeConverter para convertir el objeto Uri a String.
// Room no guarda directamente objetos de tipo Uri en la base de datos, nos admite String, Int

class UriConverters {
    @TypeConverter
    fun fromString(value: String?): Uri? = value?.let { Uri.parse(it) }

    @TypeConverter
    fun uriToString(uri: Uri?): String? = uri?.toString()
}

