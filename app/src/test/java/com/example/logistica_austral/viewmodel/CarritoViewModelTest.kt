package com.example.logistica_austral.viewmodel

import com.example.logistica_austral.model.Camion
import com.example.logistica_austral.model.Carrito
import com.example.logistica_austral.repository.CamionRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class CarritoViewModelTest : StringSpec({

    val dispatcher = StandardTestDispatcher()

    beforeTest {
        Dispatchers.setMain(dispatcher)
    }
    afterTest {
        Dispatchers.resetMain()
    }

    // Mocks de las dependencias

    val mockRepo = mockk<CamionRepository> (relaxed=true)
    val mockCarrito = mockk<Carrito> (relaxed = true)

    // Helper: Para crear camiones sin que falten datos
    fun crearCamionFalso(id: Int): Camion {
        return Camion(
            id = id,
            patente = "AA-BB-$id",
            marca = "Volvo",
            modelo = "FH",
            annio = 2024,
            tipo = "Carga",
            capacidad = 20000,
            disponibilidad = true,
            estado = "Semi-Nuevo",
            descripcion = "Camion de prueba",
            traccion = "6x4",
            precio = 41500000
        )
    }

    "init debe cargar la lista de camiones desde el repositorio" {

        val listaCamiones = listOf(crearCamionFalso(1), crearCamionFalso(2))

        coEvery { mockRepo.obtenerTodos() } returns listaCamiones

        // Simular que la función devuelve un flujo vacio para que no falle.
        every { mockCarrito.observeCamiones(any() ) } returns flowOf(emptyList())

        val viewModel = CarritoViewModel(mockCarrito, mockRepo)
        dispatcher.scheduler.advanceUntilIdle() // Esperemos a que cargue

        viewModel.allCamiones.value.size shouldBe 2
        viewModel.allCamiones.value[0].patente shouldBe "AA-BB-1"

        coVerify(exactly = 1) { mockRepo.obtenerTodos() }
    }

    "onQuitarDelCarrito debe llamar al método remove del Carrito" {
        every { mockCarrito.observeCamiones(any() ) } returns flowOf(emptyList() )
        val viewModel = CarritoViewModel(mockCarrito, mockRepo)

        val camionBorrar = crearCamionFalso(5)

        viewModel.onQuitarDelCarrito(camionBorrar)
        dispatcher.scheduler.advanceUntilIdle() // Esperemos a que cargue

        coVerify {mockCarrito.remove(5) }
    }


})