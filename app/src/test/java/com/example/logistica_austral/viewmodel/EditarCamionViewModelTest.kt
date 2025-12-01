package com.example.logistica_austral.viewmodel

import com.example.logistica_austral.model.Camion
import com.example.logistica_austral.repository.CamionRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class EditarCamionViewModelTest : StringSpec({

    val dispatcher = StandardTestDispatcher()

    beforeTest {
        Dispatchers.setMain(dispatcher)
    }

    afterTest {
        Dispatchers.resetMain()
    }

    val mockRepo = mockk<CamionRepository>(relaxed = true)

    // Helper para crear camiones y evitar errores de datos faltantes
    fun crearCamionFalso(id: Int, patente: String): Camion {
        return Camion(
            id = id,
            patente = patente,
            marca = "Scania",
            modelo = "S5",
            annio = 2023,
            tipo = "Carga",
            capacidad = 5000,
            disponibilidad = true,
            estado = "Excelente",
            descripcion = "Camion para editar",
            traccion = "4x2",
            precio = 25000000,
            imagenUri = null // Dejamos null para evitar problemas con Uri en tests
        )
    }

    "cargar(id) debe traer los datos de repositorio y llenar el UI State" {

        val viewModel = EditarCamionViewModel(mockRepo)

        val camionSimulado = crearCamionFalso(10, "ED-TR-10")

        coEvery { mockRepo.obtenerPorId(10) } returns camionSimulado

        viewModel.cargar(10)
        dispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.value.patente shouldBe "ED-TR-10"
        viewModel.uiState.value.marca shouldBe "Scania"
        viewModel.uiState.value.precio shouldBe "25000000"

        viewModel.idEditando.value shouldBe 10
    }

    "guardarCambios debe fallar si hay campos vacíos o inválidos" {
        val viewModel = EditarCamionViewModel(mockRepo)

        val camionSimulado = crearCamionFalso(20, "MA-LO-20")
        coEvery { mockRepo.obtenerPorId(20) } returns camionSimulado
        viewModel.cargar(20)
        dispatcher.scheduler.advanceUntilIdle()

        // Para el error, borramos la patente y ponemos un annio invalido
        viewModel.onPatenteChange("")
        viewModel.onAnnioChange("texto")

        viewModel.guardarCambios()

        viewModel.uiErrors.value.esErrorPatente shouldBe "Patente vacia"
        viewModel.uiErrors.value.esErrorAnnio shouldBe "Anio invalido"
        viewModel.mensaje.value shouldBe "Corrija errores"

        // Aseguramos de que NO se llamó a actualizar
        coVerify(exactly = 0 ){ mockRepo.actualizarCamion(any(), any() ) }
    }

    "guardarCambios debe ser EXITOSO si los datos son correctos" {
        val viewModel = EditarCamionViewModel(mockRepo)

        val camionOriginal = crearCamionFalso(30, "VI-JA-30")
        coEvery { mockRepo.obtenerPorId(30) } returns camionOriginal
        viewModel.cargar(30)
        dispatcher.scheduler.advanceUntilIdle()

        viewModel.onPatenteChange("NU-VA-30")
        viewModel.onMarcaChange("Marca Nueva")
        viewModel.onPrecioChange("99999")

        val camionActualizado = crearCamionFalso(30, "NU-VA-30")
        coEvery { mockRepo.actualizarCamion(any(), any()) } returns camionActualizado

        viewModel.guardarCambios()
        dispatcher.scheduler.advanceUntilIdle()

        viewModel.mensaje.value shouldBe "Cambios guardados con exito"

        coVerify(exactly = 1) { mockRepo.actualizarCamion(eq(30), any()) }
    }

    "Los métodos onChange deben actualizar el estado UI correctamente" {
        val viewModel = EditarCamionViewModel(mockRepo)

        // Ejecutamos todos los cambios de texto
        viewModel.onPatenteChange("AA-BB-11")
        viewModel.onMarcaChange("Mercedes")
        viewModel.onModeloChange("New Actros")
        viewModel.onAnnioChange("2023")
        viewModel.onTipoChange("Tracto")
        viewModel.onCapacidadChange("10000")
        viewModel.onEstadoChange("Reparacion")
        viewModel.onDescripcionChange("Detalle")
        viewModel.onTraccionChange("6x2")
        viewModel.onPrecioChange("100")
        viewModel.onDisponibilidadChange(false)

        // Verificamos que se guardaron en el State
        viewModel.uiState.value.patente shouldBe "AA-BB-11"
        viewModel.uiState.value.marca shouldBe "Mercedes"
        viewModel.uiState.value.modelo shouldBe "New Actros"
        viewModel.uiState.value.annio shouldBe "2023"
        viewModel.uiState.value.tipo shouldBe "Tracto"
        viewModel.uiState.value.capacidad shouldBe "10000"
        viewModel.uiState.value.estado shouldBe "Reparacion"
        viewModel.uiState.value.descripcion shouldBe "Detalle"
        viewModel.uiState.value.traccion shouldBe "6x2"
        viewModel.uiState.value.precio shouldBe "100"
        viewModel.uiState.value.disponibilidad shouldBe false
    }
})