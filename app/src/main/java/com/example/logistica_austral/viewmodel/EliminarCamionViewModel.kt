package com.example.logistica_austral.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logistica_austral.model.Camion
import com.example.logistica_austral.repository.CamionRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EliminarCamionViewModel(private val repository: CamionRepository): ViewModel() {
    private val _camiones = MutableStateFlow<List<Camion>>(emptyList())
    val camiones: StateFlow<List<Camion>> = _camiones

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        refrescar(simularCarga = true)
    }

    fun refrescar(simularCarga: Boolean = false) = viewModelScope.launch {
        _isLoading.value = true
        val data = repository.obtenerTodos()
        if (simularCarga) {
            delay(800) // simula tiempo de carga
        }
        _camiones.value = data
        _isLoading.value = false
    }

    fun eliminar(camion: Camion) = viewModelScope.launch {
        repository.eliminar(camion)
        // recargar lista luego de eliminar, sin demora artificial
        refrescar(simularCarga = false)
    }
}
