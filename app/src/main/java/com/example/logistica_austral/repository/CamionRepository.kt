package com.example.logistica_austral.repository

import com.example.logistica_austral.model.Camion
import com.example.logistica_austral.model.CamionDao

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
        return camionDao.obtenerCamiones()
    }
}