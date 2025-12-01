package com.example.logistica_austral.repository

import com.example.logistica_austral.model.Usuario
import com.example.logistica_austral.model.UsuarioDao
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.coVerify


class UsuarioRepositoryTest : StringSpec({

    // 1. Mock del DAO
    val mockDao = mockk<UsuarioDao>(relaxed = true)

    // 2. Instanciamos el repositorio, pasando el mock
    val repository = UsuarioRepository(mockDao)

    "login debe llamar al DAO y devolver el usuario si las credenciales son correctas" {
        val correo = "admin@example.com"
        val pass = "1234"
        val usuarioEsperado = Usuario(1, "Admin", correo, pass)

        // Cuando te pidan login con estos datos, devuelve el usuario
        coEvery { mockDao.login(correo,pass) }returns usuarioEsperado

        // Ejecutar
        val resultado = repository.login(correo,pass)

        resultado shouldBe usuarioEsperado

        // verificamos que el repositorio realmente llamó a la API
        coVerify(exactly = 1) {mockDao.login(correo,pass) }

    }

    "insertar debe llamar al DAO correctamente" {
        val userNew = Usuario(2, "Alvaro", "alvaro@duoc.cl", "1234")

        // Como la función insertar no devuelve nada, usamos justRun
        coEvery {mockDao.insertar(userNew) } returns Unit

        repository.insertar(userNew)

        // Verificamos que el Dao recibió la orden insertar
        coVerify(exactly = 1) {mockDao.insertar(userNew) }

    }

    "login debe devolver null si el DAO falla o devuelve null" {
        // Devuelve null para cualguier login
        coEvery { mockDao.login(any(), any() ) } returns null

        val resultado = repository.login("error@example.com", "passwm")

        resultado shouldBe null
    }
})
