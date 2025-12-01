package com.example.logistica_austral.viewmodel

import com.example.logistica_austral.model.Usuario
import com.example.logistica_austral.repository.UsuarioRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest: StringSpec({

    // Config de corrutinas para el ViewModel
    val dispatcher = StandardTestDispatcher()
    beforeTest {
        Dispatchers.setMain(dispatcher)
    }
    afterTest{
        Dispatchers.resetMain()
    }

    "iniciarSesion debe cambiar loginExitoso a true con credenciales correctas" {
        // 1. Preparar los datos de prueba
        val correoPrueba = "admin@duoc.cl"
        val passPrueba = "1234"
        val usuarioSimulado = Usuario(1, "Admin", correoPrueba, passPrueba)

        // 2. Mock: Simulados el repositorio (para no usar la bd real)
        val mockRepo = mockk<UsuarioRepository>()

        // Cuando llamen al login con los datos, devuelve el usuarioSimulado
        coEvery {mockRepo.login(correoPrueba, passPrueba) } returns usuarioSimulado

        // 3. Instanciar el viewModel con el mock
        val viewModel = LoginViewModel(mockRepo)

        // Llenamos los campos del estado (simulando que el user escribió)
        viewModel.onCorreoChange(correoPrueba)
        viewModel.onPasswordChange(passPrueba)

        // 4. Ejecutar la acción (When)
        viewModel.iniciarSesion()

        // Avanzamos el despachador de corrutinas para que se ejecute el viewModelScope
        dispatcher.scheduler.advanceUntilIdle()

        // 5. Validar el resultado (Then)
        viewModel.loginExitoso.value shouldBe true
        viewModel.mensaje.value shouldBe "¡Bienvenido Admin!"
    }

    "iniciarSesion debe fallar si las credenciales son incorrectas" {
        // 1. Preparar
        val mockRepo = mockk<UsuarioRepository>()
        // Simulamos que el repo devuelve un null
        coEvery { mockRepo.login(any(), any()) } returns null

        val viewModel = LoginViewModel(mockRepo)
        viewModel.onCorreoChange("error@example.com")
        viewModel.onPasswordChange("11111")

        // 2. Ejecutar
        viewModel.iniciarSesion()
        dispatcher.scheduler.advanceUntilIdle()

        // 3. Validar
        viewModel.loginExitoso.value shouldBe false
        viewModel.mensaje.value shouldBe "Error: Correo o contraseña incorrectos."
    }
})