package com.example.logistica_austral.viewmodel

import com.example.logistica_austral.repository.UsuarioRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.coVerify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import java.lang.RuntimeException

@OptIn(ExperimentalCoroutinesApi::class)
class RegistroViewModelTest : StringSpec({

    // Config de corrutinas
    val dispatcher = StandardTestDispatcher()

    beforeTest {
        Dispatchers.setMain(dispatcher)
    }
    afterTest {
        Dispatchers.resetMain()
    }

    // 1. Mock del repositorio
    val mockRepo = mockk<UsuarioRepository> (relaxed = true)

    "OnChanges deben actualizar el estado UI correctamente" {
        val viewModel = RegistroViewModel(mockRepo)

        viewModel.onNombreChange("Juan")
        viewModel.onCorreoChange("juan@example.com")
        viewModel.onPasswordChange("123456")

        viewModel.uiState.value.nombre shouldBe "Juan"
        viewModel.uiState.value.correo shouldBe "juan@example.com"
        viewModel.uiState.value.contrasena shouldBe "123456"
    }

    "registrarUsuario debe fallar si el nombre está vacío" {
        val viewModel = RegistroViewModel(mockRepo)

        // Dejamos el nombre vacio
        viewModel.onNombreChange("")

        viewModel.registrarUsuario()

        // Validar que marcó el error
        viewModel.uiErrors.value.esErrorNombre shouldBe "El nombre es obligatorio"

        // Validamos que NO llamó al repositorio
        coVerify(exactly = 0) {mockRepo.insertar(any() ) }
    }

    "registrarUsuario debe fallar si la contraseña es muy corta" {
        val viewModel = RegistroViewModel(mockRepo)

        viewModel.onNombreChange("Juan")
        viewModel.onCorreoChange("juan@example.com")
        viewModel.onPasswordChange("123") // Menos de 4 cars.

        viewModel.registrarUsuario()

        viewModel.uiErrors.value.esErrorContrasena shouldBe "Debe tener al menos 4 caracteres"
        coVerify(exactly = 0) { mockRepo.insertar(any() ) }

    }

    "registrarUsuario debe ser EXITOSO si todo los datos son válidos" {

        val viewModel = RegistroViewModel(mockRepo)
        val correo = "juan@example.com"
        val pass = "12345"
        val nombre = "Juan García"

        // Llenamos el form
        viewModel.onNombreChange(nombre)
        viewModel.onCorreoChange(correo)
        viewModel.onPasswordChange(pass)

        // Simulamos el repositorio
        coEvery { mockRepo.insertar(any() ) } returns Unit

        // Eject
        viewModel.registrarUsuario()
        dispatcher.scheduler.advanceUntilIdle()

        // Validar
        viewModel.mensaje.value shouldBe "¡Usuario registrado con éxito!"
        viewModel.registroExitoso.value shouldBe true

        // Verificar que se mandó al guardar
        coVerify(exactly = 1) { mockRepo.insertar(any() ) }
    }

    "registrarUsuario debe manejar errores del repositorio (try-catch)" {
        val viewModel = RegistroViewModel(mockRepo)

        viewModel.onNombreChange("Juan")
        viewModel.onCorreoChange("error@example.com")
        viewModel.onPasswordChange("12345")

        // Simulamos que la base de datos falla
        coEvery { mockRepo.insertar(any() ) } throws RuntimeException("Fallo BD")

        viewModel.registrarUsuario()
        dispatcher.scheduler.advanceUntilIdle()

        viewModel.mensaje.value shouldBe "Error al registrar: Fallo BD"
        viewModel.registroExitoso.value shouldBe false
    }
})