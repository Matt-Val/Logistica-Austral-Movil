package com.example.logistica_austral.repository

import com.example.logistica_austral.data.remote.RetrofitInstance
import com.example.logistica_austral.model.Camion
import com.example.logistica_austral.model.CamionDao
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import com.google.gson.Gson
import java.io.File

// El Repositorio recibe el DAO en su constructor
// para poder acceder a las funciones de la base de datos.
class CamionRepository(private val camionDao: CamionDao) {

    // Esta función llama a la función 'insertar' del DAO.
    // La marcamos como 'suspend' porque el DAO es asíncrono.
    suspend fun insertar(camion: Camion) {
        camionDao.insertar(camion)
    }

    // Inserta una lista de camiones (utilidad para sembrar datos demo)
    suspend fun insertarTodos(camiones: List<Camion>) {
        camiones.forEach { camionDao.insertar(it) }
    }

    // Obtiene todos los camiones ordenados por patente DESC
    suspend fun obtenerTodos(): List<Camion> {
        return RetrofitInstance.api.getCamiones()
    }

    // Elimina un camión existente
    suspend fun eliminar(camion: Camion) {
        try {
            RetrofitInstance.api.deleteCamion(camion.id)
        } catch (e: Exception) {
            // caso de error
        }
    }

    // Crea un camión en el servidor remoto, incluyendo una imagen
    suspend fun crearRemotoConImagen(camion: Camion, imagenFile: File?): Camion {
        require(imagenFile != null && imagenFile.exists()) { "Debe proporcionar un archivo de imagen válido" }
        val gson = Gson()
        val camionJson = gson.toJson(camion)
        val camionBody: RequestBody = camionJson.toRequestBody("application/json".toMediaType())
        val fileBody = imagenFile.asRequestBody("image/*".toMediaTypeOrNull())
        val multipart = MultipartBody.Part.createFormData("file", imagenFile.name, fileBody)
        return RetrofitInstance.api.createCamionWithImage(camionBody, multipart)
    }
}