package com.example.logistica_austral.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logistica_austral.model.Camion
import com.example.logistica_austral.repository.CamionRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VerCamionViewModel(private val repository: CamionRepository): ViewModel() {
    private val _camiones = MutableStateFlow<List<Camion>>(emptyList())
    val camiones: StateFlow<List<Camion>> = _camiones

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    // estado de error: api
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        refrescar(simularCarga = true)
    }

    fun refrescar(simularCarga: Boolean = false) = viewModelScope.launch {
        _isLoading.value = true
        try {
            val data = repository.obtenerTodos()
            if (simularCarga) {
                delay(800) // simula tiempo de carga
            }
            _camiones.value = data
        } catch (e: Exception) {
            _camiones.value = emptyList()
            _error.value = "Error al cargar. Intenta mas tarde"
        }
        _isLoading.value = false
    }

    fun eliminar(camion: Camion) = viewModelScope.launch {
        try {
            repository.eliminar(camion)
            refrescar(simularCarga = false)
        } catch (e: Exception) {
            _error.value = "Error al eliminar. Intenta mas tarde"
        }
    }

    fun clearError() { _error.value = null }
}
