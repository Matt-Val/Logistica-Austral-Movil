package com.example.logistica_austral.model


// DEMOSTRACION SOLAMENTE TEMPORAL: 6 productos tipo Camion: todavia no esta la base de datos
object CamionSampleData {
    val items = listOf(
        Camion(
            id = 1,
            patente = "ABC123",
            marca = "Scania",
            modelo = "S V8 770S",
            annio = "2022",
            tipo = "Tracto",
            capacidad = 40,
            disponibilidad = true,
            estado = "Disponible",
            descripcion = "Camion potente y moderno",
            traccion = "4x2"
        ),
        Camion(
            id = 2,
            patente = "DEF456",
            marca = "Mercedes-Benz",
            modelo = "Actros 2653",
            annio = "2021",
            tipo = "Tracto",
            capacidad = 38,
            disponibilidad = true,
            estado = "Disponible",
            descripcion = "Camion eficiente y confiable",
            traccion = "6x4"
        ),
        Camion(
            id = 3,
            patente = "GHI789",
            marca = "Volvo",
            modelo = "FH16 750",
            annio = "2020",
            tipo = "Tracto",
            capacidad = 42,
            disponibilidad = false,
            estado = "En mantenimiento",
            descripcion = "Camion robusto para cargas pesadas",
            traccion = "6x2"
        ),
        Camion(
            id = 4,
            patente = "JKL012",
            marca = "MAN",
            modelo = "TGX 18.510",
            annio = "2019",
            tipo = "Tracto",
            capacidad = 36,
            disponibilidad = true,
            estado = "Disponible",
            descripcion = "Camion versatil y moderno",
            traccion = "4x2"
        ),
        Camion(
            id = 5,
            patente = "MNO345",
            marca = "Iveco",
            modelo = "S-Way 570",
            annio = "2023",
            tipo = "Tracto",
            capacidad = 39,
            disponibilidad = false,
            estado = "En ruta",
            descripcion = "Camion nuevo y eficiente",
            traccion = "6x4"
        ),
        Camion(
            id = 6,
            patente = "PQR678",
            marca = "DAF",
            modelo = "XF 530",
            annio = "2021",
            tipo = "Tracto",
            capacidad = 37,
            disponibilidad = true,
            estado = "Disponible",
            descripcion = "Camion confiable para largas distancias",
            traccion = "4x2"
        )
    )
}