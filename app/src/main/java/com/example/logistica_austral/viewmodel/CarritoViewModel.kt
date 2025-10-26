package com.example.logistica_austral.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logistica_austral.model.Camion
import com.example.logistica_austral.model.CamionSampleData
import com.example.logistica_austral.model.Carrito
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch // necesario para lanzar corrutinas

class CarritoViewModel(private val carrito: Carrito) : ViewModel() {

    private val _allCamiones = MutableStateFlow(CamionSampleData.items) // para la "demo" de items, stateflow mutable

    // stateflow inmutable expuesto para ver la lista de items (camiones)
    val allCamiones: StateFlow<List<Camion>> = _allCamiones.asStateFlow()

    // stateflow que contiene los camiones en el carrito, observando cambios en allcamiones
    val camionesEnCarrito: StateFlow<List<Camion>> =
        carrito.observeCamiones(allCamiones)
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // NUEVO: accion para quitar un camion del carrito
    fun onQuitarDelCarrito(camion: Camion) {
        viewModelScope.launch {
            carrito.remove(camion.id)
        }
    }
}