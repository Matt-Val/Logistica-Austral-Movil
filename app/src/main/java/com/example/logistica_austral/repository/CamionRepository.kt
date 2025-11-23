package com.example.logistica_austral.repository

import com.example.logistica_austral.data.model.CamionDto
import com.example.logistica_austral.data.remote.RetrofitInstance
import com.example.logistica_austral.model.Camion
import com.example.logistica_austral.model.CamionDao
import com.google.gson.Gson
import java.io.File
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

// nota: MIME multipurpose Internet Mail Extensions es un tipo de identificador que
//      indica el formato de un archivo transmitido por internet,

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

    // Funcion para crear un camion en el servidor remoto usando multipart (json + imagen)
    suspend fun crearRemotoConImagen(dto: CamionDto, imagenFile: File?): Camion {
        // verifica que el archivo de imagen exista
        require(imagenFile != null && imagenFile.exists()) { "debe proporcionar un archivo de imagen valido" }
        // convierte el dto a json usando gson
        val gson = Gson()
        val camionJson = gson.toJson(dto)
        // crea el requestbody para la parte json del multipart
        val camionBody: RequestBody = camionJson.toRequestBody("application/json".toMediaType())

        // determina el tipo mime segun la extension del archivo
        val mime = when (imagenFile.extension.lowercase()) {
            "png" -> "image/png"        // -> indica que es una imagen .png
            "jpg", "jpeg" -> "image/jpeg"   // -> indica que es .jpeg
            else -> "image/*"
        }.toMediaTypeOrNull()
        // crea el requestbody para la parte de archivo
        val fileBody = imagenFile.asRequestBody(mime)
        // crea la parte multipart para el archivo con el nombre esperado por el backend
        val multipart = MultipartBody.Part.createFormData("file", imagenFile.name, fileBody)
        try {
            // llama al endpoint remoto usando retrofit y retorna el camion creado
            return RetrofitInstance.api.createCamionWithImage(camionBody, multipart)

        } catch (e: HttpException) {
            // si ocurre un error http, lanza una excepcion con el codigo y mensaje
            throw RuntimeException("http ${e.code()} al crear: ${e.message()}")
        }
    }
}