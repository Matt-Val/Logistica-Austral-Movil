package com.example.logistica_austral.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("http://35.171.234.213:8080/api/v1/")
            .addConverterFactory(GsonConverterFactory.create()) // convierte el JSON
            .build()
            .create(ApiService::class.java) // aqui implementa la interface ApiService
    }

}