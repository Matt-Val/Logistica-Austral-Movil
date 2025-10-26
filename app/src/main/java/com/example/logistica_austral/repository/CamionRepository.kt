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

    // Se pueden agregar mas funciones como esta
    // suspend fun obtenerTodos(): List<Camion> {
    //    return camionDao.obtenerTodos()
    // }
}