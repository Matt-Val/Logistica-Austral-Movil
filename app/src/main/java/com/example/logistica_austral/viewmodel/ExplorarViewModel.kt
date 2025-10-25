package com.example.logistica_austral.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logistica_austral.model.Camion
import com.example.logistica_austral.model.CamionSampleData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExplorarViewModel : ViewModel() {

    private val _camiones = MutableStateFlow<List<Camion>>(emptyList())
    val camiones: StateFlow<List<Camion>> = _camiones

    init {
        // carga la “demo” (luego con DAO sin tocar la UI)
        viewModelScope.launch {
            _camiones.value = CamionSampleData.items
        }
    }

    // Placeholder para el boton (pensando en carrito futuro)
    fun onAgregarACarrito(camion: Camion) {
        // de momento no persistira
    }
}