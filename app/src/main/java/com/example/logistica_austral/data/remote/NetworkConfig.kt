package com.example.logistica_austral.data.remote

// fuente unica de configuracion de endpoints del backend
object NetworkConfig {



    // URL raiz del servidor (sin sufijo de API) para recursos estaticos como mis imagenes
    const val SERVER_BASE_URL: String = "http://35.171.234.213:8080"


    // Base para las llamadas REST de la API
    const val API_BASE_URL: String = "$SERVER_BASE_URL/api/v1/"



}

