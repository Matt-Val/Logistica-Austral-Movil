package com.example.logistica_austral.viewmodel

import androidx.lifecycle.ViewModel
import com.example.logistica_austral.model.Camion
import com.example.logistica_austral.model.CamionErrores
import com.example.logistica_austral.model.CamionUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import androidx.lifecycle.viewModelScope
import com.example.logistica_austral.repository.CamionRepository
import kotlinx.coroutines.launch

class CamionViewModel(private val repository: CamionRepository) : ViewModel() {
    // 1. Declarar los estados
    private val _uiState = MutableStateFlow(CamionUIState())
    val uiState: StateFlow<CamionUIState> = _uiState.asStateFlow()
    private val _uiErrors = MutableStateFlow(CamionErrores())
    val uiErrors: StateFlow<CamionErrores> = _uiErrors.asStateFlow()
    private val _mensaje = MutableStateFlow("")
    val mensaje: StateFlow<String> = _mensaje.asStateFlow()


    // 2. Funciones "OnChange" para cada campo
    fun onPatenteChange(patente: String) {
        _uiState.update { it.copy(patente = patente) }
        _uiErrors.update { it.copy(esErrorPatente = null) }
    }
    fun onMarcaChange(marca: String) {
        _uiState.update { it.copy(marca= marca) }
        _uiErrors.update { it.copy(esErrorMarca = null) }
    }
    fun onModeloChange(modelo: String) {
        _uiState.update { it.copy(modelo = modelo) }
        _uiErrors.update { it.copy(esErrorModelo = null) }
    }
    fun onAnnioChange(annio : String) {
        _uiState.update { it.copy(annio = annio) }
        _uiErrors.update {it.copy(esErrorAnnio = null) }
    }
    fun onTipoChange(tipo : String) {
        _uiState.update { it.copy(tipo = tipo ) }
        _uiErrors.update { it.copy(esErrorTipo = null) }
    }
    fun onCapacidadChange(capacidad : String) {
        _uiState.update { it.copy(capacidad = capacidad) }
        _uiErrors.update { it.copy(esErrorCapacidad = null) }
    }
    fun onDisponibilidadChange(disponibilidad : Boolean) {
        _uiState.update { it.copy(disponibilidad = disponibilidad) }
    }
    fun onEstadoChange(estado : String) {
        _uiState.update { it.copy(estado = estado) }
        _uiErrors.update { it.copy(esErrorEstado = null) }
    }
    fun onDescripcionChange(descripcion : String) {
        _uiState.update { it.copy(descripcion = descripcion) }
        _uiErrors.update { it.copy(esErrorDescripcion = null) }
    }
    fun onTraccionChange(traccion : String) {
        _uiState.update { it.copy(traccion = traccion) }
        _uiErrors.update { it.copy(esErrorTraccion = null) }
    }
    fun onPrecioChange(precio: String) {
        _uiState.update { it.copy(precio = precio) }
        _uiErrors.update { it.copy(esErrorPrecio = null) }
    }
    // 3.- Validacion
    private fun validarFormulario():Boolean {
        val state = _uiState.value
        // Reseteamos errores antes de validar
        _uiErrors.value = CamionErrores()
        var esValido = true

        if (state.patente.isBlank()) {
            _uiErrors.update { it.copy(esErrorPatente = "La patente no puede estar vacía") }
            esValido = false
        }
        if (state.marca.isBlank()) {
            _uiErrors.update { it.copy(esErrorMarca = "La marca no puede estar vacía") }
            esValido = false
        }
        if (state.modelo.isBlank()) {
            _uiErrors.update { it.copy(esErrorModelo = "El modelo no puede estar vacío") }
            esValido = false
        }
        if (state.tipo.isBlank()) {
            _uiErrors.update { it.copy(esErrorTipo = "El tipo no puede estar vacío") }
            esValido = false
        }
        if (state.estado.isBlank()) {
            _uiErrors.update { it.copy(esErrorEstado = "El estado no puede estar vacío") }
            esValido = false
        }
        if (state.descripcion.isBlank()) {
            _uiErrors.update { it.copy(esErrorDescripcion = "La descripción no puede estar vacía") }
            esValido = false
        }
        if (state.traccion.isBlank()) {
            _uiErrors.update { it.copy(esErrorTraccion = "La tracción no puede estar vacía")} // Corregido "vacío" a "vacía"
            esValido = false
        }

        // Validación por números
        if (state.annio.isBlank()) {
            _uiErrors.update { it.copy(esErrorAnnio = "El año no puede estar vacío") }
            esValido = false
        } else if (state.annio.toIntOrNull() == null) {
            _uiErrors.update { it.copy(esErrorAnnio = "El año debe ser un número válido") }
            esValido = false
        }

        if (state.capacidad.isBlank()) {
            _uiErrors.update { it.copy(esErrorCapacidad = "La capacidad no debe estar vacía") }
            esValido = false
        } else if (state.capacidad.toIntOrNull() == null) {
            _uiErrors.update { it.copy(esErrorCapacidad = "La capacidad debe ser un número válido") }
            esValido = false
        }

        if (state.precio.isBlank()) {
            _uiErrors.update { it.copy(esErrorPrecio = "El precio no debe estar vacío") }
            esValido = false
        } else if (state.precio.toIntOrNull() == null) {
            _uiErrors.update { it.copy(esErrorPrecio = "El precio debe ser un número válido") }
            esValido = false
        }
        return esValido
    }

    // 4.- Función de Registro
    fun registrarCamion() {
        if (validarFormulario()) {
            val state = _uiState.value

            // La validacion asegura que los campos no esten nulos ni vacios.
            // Usamos toIntOrNull con un valor predeterminado como medida de seguridad.
            val annioInt = state.annio.toIntOrNull() ?: 0
            val capacidadInt = state.capacidad.toIntOrNull() ?: 0
            val precioInt = state.precio.toIntOrNull() ?: 0

            val nuevoCamion = Camion(
                patente = state.patente,
                marca = state.marca,
                modelo = state.modelo,
                annio = annioInt,
                tipo = state.tipo,
                capacidad = capacidadInt,
                disponibilidad = state.disponibilidad,
                estado = state.estado,
                descripcion = state.descripcion,
                traccion = state.traccion,
                precio = precioInt
            )
            viewModelScope.launch{
                repository.insertar(nuevoCamion)
                _mensaje.value = "Camion ${nuevoCamion.patente} registrado con éxito!"
            }
        } else {
            _mensaje.value= "Error: Revise todos los campos."
        }
    }
}
