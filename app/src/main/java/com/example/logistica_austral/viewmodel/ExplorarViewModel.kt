package com.example.logistica_austral.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logistica_austral.model.Camion
import com.example.logistica_austral.model.CamionSampleData
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

    private val _camiones = MutableStateFlow<List<Camion>>(emptyList())
    val camiones: StateFlow<List<Camion>> = _camiones

    private val _isLoadingCart = MutableStateFlow(true)
    val isLoadingCart: StateFlow<Boolean> = _isLoadingCart

    init {
        viewModelScope.launch {
            try {
                // desde la api
                _camiones.value = repository.obtenerTodos()

            } catch (e: Exception) {
                _camiones.value = emptyList() // evita crash
            }
            delay(800)
            _isLoadingCart.value = false
        }
    }

    // para agregar camion por id, al carrito
    fun onAgregarACarrito(camion: Camion) = viewModelScope.launch {
        try { carrito.add(camion.id) } catch (_: Exception) { }
    }
}