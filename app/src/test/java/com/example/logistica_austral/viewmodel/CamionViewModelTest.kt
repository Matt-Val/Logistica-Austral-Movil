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
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
class CamionViewModelTest : StringSpec({

    // Config de corrutinas
    val dispatcher = StandardTestDispatcher()

    beforeTest {
        Dispatchers.setMain(dispatcher)
    }

    afterTest {
        Dispatchers.resetMain()
    }

    // Mock del repositorio y del archivo de img
    val mockRepo = mockk<CamionRepository> (relaxed = true)
    val mockFile = mockk<File> (relaxed = true)

    // Helper: Crea un camión con todos los datos rellenos para evitar errores
    fun crearCamionFalso(id: Int, patente: String): Camion {
        return Camion(
            id = id,
            patente = patente,
            marca = "Scania",
            modelo = "R500",
            annio = 2024,
            tipo = "Carga",
            capacidad = 20000,
            disponibilidad = true,
            estado = "Nuevo",
            descripcion = "Camion de Test",
            traccion = "6x4",
            precio = 90000000
        )
    }

    "Los OnChange deben actualizar el estado UI" {
        val viewModel = CamionViewModel(mockRepo)

        viewModel.onPatenteChange("AA-BB-99")
        viewModel.onPrecioChange("500000")
        viewModel.onDisponibilidadChange(true)

        viewModel.uiState.value.patente shouldBe "AA-BB-99"
        viewModel.uiState.value.precio shouldBe "500000"
        viewModel.uiState.value.disponibilidad shouldBe true
    }

    "registrarCamion debe fallar si hay campos vacíos" {
        val viewModel = CamionViewModel(mockRepo)

        // Dejamos todo vacío y tratamos de registrar
        viewModel.registrarCamionRemotoConImagen(mockFile)

        // Verificamos que saltaron los errores
        viewModel.uiErrors.value.esErrorPatente shouldBe "La patente no puede estar vacía"
        viewModel.uiErrors.value.esErrorMarca shouldBe "La marca no puede estar vacía"
        viewModel.mensaje.value shouldBe "Error: Revise todos los campos."

        // Aseguramos de que no llamó al repositorio
        coVerify(exactly = 0) {mockRepo.crearRemotoConImagen(any(), any() ) }
    }

    "registrarCamion debe fallar si los números no son válidos" {
        val viewModel = CamionViewModel(mockRepo)

        // llenamos los datos obligatorios de texto
        viewModel.onPatenteChange("AA-BB-10")
        viewModel.onMarcaChange("Volvo")
        viewModel.onModeloChange("FH")
        viewModel.onTipoChange("Tolva")
        viewModel.onEstadoChange("Bueno")
        viewModel.onDescripcionChange("desc")
        viewModel.onTraccionChange("4x4")

        // Para el error, ponemos texto donde van números
        viewModel.onAnnioChange("dos mil")
        viewModel.onPrecioChange("muy caro")

        viewModel.registrarCamionRemotoConImagen(mockFile)

        // Validamos mensajes específicos
        viewModel.uiErrors.value.esErrorAnnio shouldBe "El año debe ser un número válido"
        viewModel.uiErrors.value.esErrorPrecio shouldBe "El precio debe ser un número válido"
    }

    "registrarCamion debe fallar si no se adjunta imagen (File es null)" {
        val viewModel = CamionViewModel(mockRepo)

        // Llenamos todo correcto
        viewModel.onPatenteChange("AA-BB-11")
        viewModel.onMarcaChange("Volvo")
        viewModel.onModeloChange("FH")
        viewModel.onAnnioChange("2024")
        viewModel.onTipoChange("Carga")
        viewModel.onCapacidadChange("20000")
        viewModel.onEstadoChange("Nuevo")
        viewModel.onDescripcionChange("Camion de Test")
        viewModel.onTraccionChange("4x2")
        viewModel.onPrecioChange("9000000")

        // Enviamos el null en el archivo
        viewModel.registrarCamionRemotoConImagen(null)

        viewModel.mensaje.value shouldBe "Debe seleccionar o tomar una imagen"
    }

    "registrarCamion debe ser EXITOSO con datos correctos e imagen" {
        val viewModel = CamionViewModel(mockRepo)

        // 1. Llenamos el formulario completo
        viewModel.onPatenteChange("AA-BB-06")
        viewModel.onMarcaChange("Volvo")
        viewModel.onModeloChange("FH")
        viewModel.onAnnioChange("2024")
        viewModel.onTipoChange("Carga")
        viewModel.onCapacidadChange("20000")
        viewModel.onEstadoChange("Nuevo")
        viewModel.onDescripcionChange("Camion de Test")
        viewModel.onTraccionChange("6x4")
        viewModel.onPrecioChange("9000000")

        // 2. Simulamos la respuesta exitosa del repositorio
        // Creamos el camion falso de respuesta
        val camionRespuesta = crearCamionFalso(1, "AA-BB-99")
        coEvery{ mockRepo.crearRemotoConImagen(any(), any() ) } returns camionRespuesta

        // 3. Ejecutar
        viewModel.registrarCamionRemotoConImagen(mockFile)
        dispatcher.scheduler.advanceUntilIdle() // Espera la corrutina

        // 4. Validar
        viewModel.mensaje.value shouldBe "Camión AA-BB-99 creado con éxito!"

        // Verificar que se llamó al repo
        coVerify(exactly = 1) { mockRepo.crearRemotoConImagen(any(), any() ) }
    }
})