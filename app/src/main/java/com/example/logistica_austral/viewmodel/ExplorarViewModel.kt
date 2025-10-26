package com.example.logistica_austral.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logistica_austral.model.Camion
import com.example.logistica_austral.model.CamionSampleData
import com.example.logistica_austral.model.Carrito
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExplorarViewModel(private val carrito: Carrito) : ViewModel() {

    private val _camiones = MutableStateFlow<List<Camion>>(emptyList())
    val camiones: StateFlow<List<Camion>> = _camiones

    private val _isLoadingCart = MutableStateFlow(true)
    val isLoadingCart: StateFlow<Boolean> = _isLoadingCart

    init {
        // carga la “demo” (luego con DAO sin tocar la UI)
        viewModelScope.launch {
            _camiones.value = CamionSampleData.items

            // con esto simula la carga de elementos del carrito de persistencia
            delay(800)
            _isLoadingCart.value = false
        }
    }

    // para agregar camion por id, al carrito
    fun onAgregarACarrito(camion: Camion) = viewModelScope.launch {
        carrito.add(camion.id)
    }
}