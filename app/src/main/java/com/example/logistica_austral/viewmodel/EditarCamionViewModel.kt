package com.example.logistica_austral.viewmodel

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logistica_austral.data.model.CamionDto
import com.example.logistica_austral.model.CamionErrores
import com.example.logistica_austral.model.CamionUIState
import com.example.logistica_austral.repository.CamionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditarCamionViewModel(private val repository: CamionRepository): ViewModel() {
    private val _uiState = MutableStateFlow(CamionUIState())
    val uiState: StateFlow<CamionUIState> = _uiState.asStateFlow()

    // indicador para la carga inicial de datos
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()
    private val _uiErrors = MutableStateFlow(CamionErrores())
    val uiErrors: StateFlow<CamionErrores> = _uiErrors.asStateFlow()
    private val _mensaje = MutableStateFlow("")
    val mensaje: StateFlow<String> = _mensaje.asStateFlow()
    private val _idEditando = MutableStateFlow<Int?>(null)
    val idEditando: StateFlow<Int?> = _idEditando.asStateFlow()

    // reutilizamos setters similares
    fun onPatenteChange(v: String) {
        _uiState.update { it.copy(patente = v) }
        _uiErrors.update { it.copy(esErrorPatente = null) }
    }
    fun onMarcaChange(v: String) {
        _uiState.update { it.copy(marca = v) }
        _uiErrors.update { it.copy(esErrorMarca = null) }
    }
    fun onModeloChange(v: String) {
        _uiState.update { it.copy(modelo = v) }
        _uiErrors.update { it.copy(esErrorModelo = null) }
    }
    fun onAnnioChange(v: String) {
        _uiState.update { it.copy(annio = v) }
        _uiErrors.update { it.copy(esErrorAnnio = null) }
    }
    fun onTipoChange(v: String) {
        _uiState.update { it.copy(tipo = v) }
        _uiErrors.update { it.copy(esErrorTipo = null) }
    }
    fun onCapacidadChange(v: String) {
        _uiState.update { it.copy(capacidad = v) }
        _uiErrors.update { it.copy(esErrorCapacidad = null) }
    }
    fun onEstadoChange(v: String) {
        _uiState.update { it.copy(estado = v) }
        _uiErrors.update { it.copy(esErrorEstado = null) }
    }
    fun onDescripcionChange(v: String) {
        _uiState.update { it.copy(descripcion = v) }
        _uiErrors.update { it.copy(esErrorDescripcion = null) }
    }
    fun onTraccionChange(v: String) {
        _uiState.update { it.copy(traccion = v) }
        _uiErrors.update { it.copy(esErrorTraccion = null) }
    }
    fun onPrecioChange(v: String) {
        _uiState.update { it.copy(precio = v) }
        _uiErrors.update { it.copy(esErrorPrecio = null) }
    }
    fun onDisponibilidadChange(v: Boolean) {
        _uiState.update { it.copy(disponibilidad = v) }
    }
    fun onImagenUriChange(uri: Uri?) {
        _uiState.update { it.copy(imagenUri = uri) }
    }


    fun cargar(id: Int) {
        _idEditando.value = id
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val camion = repository.obtenerPorId(id)
                // Pre-cargar datos en el formulario
                _uiState.value = CamionUIState(
                    patente = camion.patente,
                    marca = camion.marca,
                    modelo = camion.modelo,
                    annio = camion.annio.toString(),
                    tipo = camion.tipo,
                    capacidad = camion.capacidad.toString(),
                    disponibilidad = camion.disponibilidad,
                    estado = camion.estado,
                    descripcion = camion.descripcion,
                    traccion = camion.traccion,
                    precio = camion.precio.toString(),
                    imagenUri = camion.imagenUri?.toUri()
                )
            } catch (e: Exception) {
                _mensaje.value = "Error al cargar datos"
            } finally {
                _isLoading.value = false
            }
        }
    }



    private fun validar(): Boolean {
        val s = _uiState.value
        _uiErrors.value = CamionErrores()
        var ok = true
        if (s.patente.isBlank()) {
            _uiErrors.update { it.copy(esErrorPatente = "Patente vacia") };ok = false }

        if (s.marca.isBlank()) {
            _uiErrors.update { it.copy(esErrorMarca = "Marca vacia") }; ok = false }

        if (s.modelo.isBlank()) {
            _uiErrors.update { it.copy(esErrorModelo = "Modelo vacio") }; ok = false }

        if (s.annio.isBlank() || s.annio.toIntOrNull() == null) {
            _uiErrors.update { it.copy(esErrorAnnio = "Anio invalido") }; ok = false }

        if (s.capacidad.isBlank() || s.capacidad.toIntOrNull() == null) {
            _uiErrors.update { it.copy(esErrorCapacidad = "Capacidad invalida") }; ok = false }

        if (s.precio.isBlank() || s.precio.toIntOrNull() == null) {
            _uiErrors.update { it.copy(esErrorPrecio = "Precio invalido") }; ok = false }

        return ok
    }

    fun guardarCambios() {
        val id = _idEditando.value ?: run { _mensaje.value = "Sin id"; return }
        if (!validar()) { _mensaje.value = "Corrija errores"; return }
        val s = _uiState.value
        val dto = CamionDto(
            patente = s.patente,
            marca = s.marca,
            modelo = s.modelo,
            annio = s.annio.toIntOrNull() ?: 0,
            tipo = s.tipo,
            capacidad = s.capacidad.toIntOrNull() ?: 0,
            disponibilidad = s.disponibilidad,
            estado = s.estado,
            descripcion = s.descripcion,
            traccion = s.traccion,
            precio = s.precio.toIntOrNull() ?: 0
        )
        _isSaving.value = true
        viewModelScope.launch {
            try {
                repository.actualizarCamion(id, dto)
                _mensaje.value = "Cambios guardados con exito"
            } catch (e: Exception) {
                _mensaje.value = "Error al guardar cambios"
            } finally {
                _isSaving.value = false
            }
        }
    }
}