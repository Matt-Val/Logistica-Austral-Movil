package com.example.logistica_austral.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UsuarioDao {


    @Insert
    suspend fun insertar(usuario: Usuario)

    // 1. Le pasamos un email y un password.
    // 2. Busca en la tabla "usuarios" una fila que coincida
    //    con AMBOS campos.
    // 3. Devuelve un 'Usuario?' (un Usuario o null).
    //    - Si lo encuentra, devuelve el objeto Usuario.
    //    - Si no lo encuentra, devuelve 'null'.
    @Query("SELECT * FROM usuarios WHERE correo = :email AND contrasena = :password LIMIT 1")
    suspend fun login(email: String, password: String): Usuario?
}