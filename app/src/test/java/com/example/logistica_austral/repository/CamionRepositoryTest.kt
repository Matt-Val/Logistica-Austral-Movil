package com.example.logistica_austral.repository

import com.example.logistica_austral.data.model.CamionDto
import com.example.logistica_austral.data.remote.ApiService
import com.example.logistica_austral.data.remote.RetrofitInstance
import com.example.logistica_austral.model.Camion
import com.example.logistica_austral.model.CamionDao
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject

class CamionRepositoryTest : StringSpec({

    // Se prepara los mocks
    val mockDao = mockk<CamionDao> (relaxed=true)
    val mockApi = mockk<ApiService> (relaxed=true)

    // Instanciamos el repositorio
    val repository = CamionRepository(mockDao)

    //
    beforeTest {
        // "Vigila" el objeto Retrofit
        mockkObject(RetrofitInstance)

        // Cuandoa alguien lo pida, dale a mockApi falso
        every {RetrofitInstance.api } returns mockApi
    }
    afterTest {
        unmockkObject(RetrofitInstance)
    }

    // Funcion que crea el camion completo sin repetir el codigo
    fun crearCamionPrueba(id: Int, patente: String): Camion {
        return Camion(
            id = id,
            patente = patente,
            marca = "Volvo",
            modelo = "FH16",
            annio = 2023,
            tipo = "Tracto",
            capacidad = 20000,
            disponibilidad = true,
            estado = "Excelente",
            descripcion = "Camión de prueba",
            traccion = "4x2",
            precio = 90000000
        )
    }

    "Obtenertodos debe devolver la lista simulada por la API" {
        // Usamos la funcion de ayuda para llenar todos los datos
        val listaSimulada = listOf(
            crearCamionPrueba(1, "AA-BB-12"),
            crearCamionPrueba(2, "AA-BB-13")
        )

        // Simulamos una respuesta
        coEvery { mockApi.getCamiones() } returns listaSimulada

        val resultado = repository.obtenerTodos()

        resultado.size shouldBe 2
        resultado[0].patente shouldBe "AA-BB-12"

        coVerify { mockApi.getCamiones() }
    }

    "eliminar debe llamar al endpoint delete" {
        val camion = crearCamionPrueba(10, "DE-LL-10")
        coEvery { mockApi.deleteCamion(10) } returns Unit

        repository.eliminar(camion)

        coVerify { mockApi.deleteCamion(10) }
    }

    "obtenerPorId debe devolver el camión específico" {
        val camionEsperado = crearCamionPrueba(5, "BS-CR-15")
        coEvery { mockApi.getCamion(5) } returns camionEsperado

        val resultado = repository.obtenerPorId(5)

        resultado shouldBe camionEsperado
    }

    "actualizarCamion debe enviar los datos al endpoint update" {
        val dto = CamionDto(
            patente = "UP-DT-17",
            marca = "Volvo",
            modelo = "FMX",
            annio = 2024,
            tipo = "Tolva",
            capacidad = 15000,
            disponibilidad = false,
            estado = "Mantención",
            descripcion = "Actualizado",
            traccion = "6x4",
            precio = 3500000
        )
        val camionActualizado = crearCamionPrueba(8,"UP-DT-17")

        coEvery { mockApi.updateCamion(8, dto) } returns camionActualizado

        val resultado = repository.actualizarCamion(8, dto)

        resultado shouldBe camionActualizado
    }

})