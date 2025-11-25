package com.example.logistica_austral.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logistica_austral.model.Camion
import com.example.logistica_austral.model.Carrito
import com.example.logistica_austral.repository.CamionRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExplorarViewModel(
    private val carrito: Carrito,
    private val repository: CamionRepository
) : ViewModel() {

    // datos para contacto por WhatsApp
    val whatsappNumeroEmpresa: String = "56912345678"
    val whatsappMensaje: String = "Hola, necesito ayuda con los camiones usados disponibles que estan a la venta."

    private val _camiones = MutableStateFlow<List<Camion>>(emptyList())
    val camiones: StateFlow<List<Camion>> = _camiones

    private val _isLoadingCart = MutableStateFlow(true)
    val isLoadingCart: StateFlow<Boolean> = _isLoadingCart

    // nuevo estado de error para mostrar mensaje si la API falla
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        viewModelScope.launch {
            try {
                // desde la api
                _camiones.value = repository.obtenerTodos()

            } catch (_: Exception) {
                // evita crash y expone mensaje para la UI
                _camiones.value = emptyList()
                _error.value = "Error al cargar. Intenta mas tarde"
            }
            delay(800)
            _isLoadingCart.value = false
        }
    }

    // para agregar camion por id, al carrito
    fun onAgregarACarrito(camion: Camion) = viewModelScope.launch {
        try { carrito.add(camion.id) } catch (_: Exception) { }
    }

    // permitir a la UI limpiar el estado de error luego de mostrar el Toast
    fun clearError() { _error.value = null }
}