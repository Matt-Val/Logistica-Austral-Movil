package com.example.logistica_austral.data.remote

import com.example.logistica_austral.model.Camion
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    @GET("camion")
    suspend fun getCamiones(): List<Camion>

    @GET("camion/{id}")
    suspend fun getCamion(@Path("id") id: Int): Camion

    @PUT("camion/{id}")
    suspend fun updateCamion(@Path("id") id: Int, @Body camion: com.example.logistica_austral.data.model.CamionDto): Camion

    @DELETE("camion/{id}")
    suspend fun deleteCamion(@Path("id") id: Int)

    @Multipart
    @POST("camion/with-image")
    suspend fun createCamionWithImage(
        @Part("camion") camion: RequestBody,
        @Part file: MultipartBody.Part
    ): Camion
}