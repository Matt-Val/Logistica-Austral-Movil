package com.example.logistica_austral.viewmodel


import com.example.logistica_austral.model.Camion
import com.example.logistica_austral.model.Carrito
import com.example.logistica_austral.repository.CamionRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class ExplorarViewModelTest : StringSpec({

    val dispatcher = StandardTestDispatcher()

    beforeTest {
        Dispatchers.setMain(dispatcher)
    }
    afterTest {
        Dispatchers.resetMain()
    }

    val mockRepo = mockk<CamionRepository> (relaxed = true)
    val mockCarrito = mockk<Carrito> (relaxed = true)

    // Helper para evitar errores de datos
    fun crearCamionFalso(id: Int): Camion {
        return Camion(
            id = id,
            patente = "TEST-$id",
            marca = "Volvo",
            modelo = "FH",
            annio = 2023,
            tipo = "Carga",
            capacidad = 20000,
            disponibilidad = true,
            estado = "Nuevo",
            descripcion = "Test",
            traccion = "6x4",
            precio = 50000000
        )
    }

    "init debe cargar camiones y apagar el loading después del delay" {

        // Simulamos la respuesta antes de instanciar el viewModel
        val lista = listOf(crearCamionFalso(1) )
        coEvery { mockRepo.obtenerTodos() } returns lista

        // EL init corre automáticamente aqui
        val viewModel = ExplorarViewModel(mockCarrito, mockRepo)

        // Verificamos estado inicial (antes de que termine el delay)
        viewModel.isLoadingCart.value shouldBe true

        dispatcher.scheduler.advanceUntilIdle()

        // validamos el estado final
        viewModel.camiones.value.size shouldBe 1
        viewModel.isLoadingCart.value shouldBe false
        viewModel.error.value shouldBe null
    }

    "init debe manejar errores (catch) si la API falla" {

        coEvery { mockRepo.obtenerTodos() } throws RuntimeException("Error API")

        val viewModel = ExplorarViewModel(mockCarrito, mockRepo)

        dispatcher.scheduler.advanceUntilIdle()

        viewModel.camiones.value shouldBe emptyList()
        viewModel.error.value shouldBe "Error al cargar. Intenta mas tarde"
        viewModel.isLoadingCart.value shouldBe false
    }

    "onAgregarCarrito debe llamar al método add del Carrito" {

        coEvery { mockRepo.obtenerTodos() } returns emptyList()
        val viewModel = ExplorarViewModel(mockCarrito, mockRepo)

        val camion = crearCamionFalso(99)
        coEvery {mockCarrito.add(99) } returns Unit

        viewModel.onAgregarACarrito(camion)
        dispatcher.scheduler.advanceUntilIdle()

        coVerify(exactly = 1) { mockCarrito.add(99) }
    }

    "clearError debe limpiar el mensaje de error" {
        coEvery { mockRepo.obtenerTodos() } throws RuntimeException("Fallo")
        val viewModel = ExplorarViewModel(mockCarrito, mockRepo)
        dispatcher.scheduler.advanceUntilIdle()

        viewModel.error.value shouldNotBe null

        viewModel.clearError()

        viewModel.error.value shouldBe null
    }
})