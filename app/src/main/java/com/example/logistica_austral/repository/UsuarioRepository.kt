package com.example.logistica_austral.repository

import com.example.logistica_austral.model.Usuario
import com.example.logistica_austral.model.UsuarioDao

// El Repositorio recibe el DAO para poder usar sus funciones
class UsuarioRepository(private val usuarioDao: UsuarioDao) {

    // Pasa la llamada de 'insertar' al DAO
    suspend fun insertar(usuario: Usuario) {
        usuarioDao.insertar(usuario)
    }
    suspend fun login(email: String, password: String): Usuario? {
        return usuarioDao.login(email, password)
    }
}