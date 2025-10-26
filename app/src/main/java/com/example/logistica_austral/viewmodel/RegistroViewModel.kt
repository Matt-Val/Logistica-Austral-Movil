package com.example.logistica_austral.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logistica_austral.model.RegistroErrores
import com.example.logistica_austral.model.RegistroUIState
import com.example.logistica_austral.model.Usuario
import com.example.logistica_austral.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegistroViewModel(private val repository: UsuarioRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(RegistroUIState())
    val uiState: StateFlow<RegistroUIState> = _uiState.asStateFlow()

    private val _uiErrors = MutableStateFlow(RegistroErrores())
    val uiErrors: StateFlow<RegistroErrores> = _uiErrors.asStateFlow()

    private val _mensaje = MutableStateFlow("")
    val mensaje: StateFlow<String> = _mensaje.asStateFlow()

    // Para avisar a la vista que el registro terminó
    private val _registroExitoso = MutableStateFlow(false)
    val registroExitoso: StateFlow<Boolean> = _registroExitoso.asStateFlow()

    // Funciones OnChange
    fun onNombreChange(nombre: String) {
        _uiState.update { it.copy(nombre = nombre) }
        _uiErrors.update { it.copy(esErrorNombre = null) }
    }
    fun onCorreoChange(correo: String) {
        _uiState.update { it.copy(correo = correo) }
        _uiErrors.update { it.copy(esErrorCorreo = null) }
    }
    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(contrasena = password) }
        _uiErrors.update { it.copy(esErrorContrasena = null) }
    }

    // Función Principal de Registro
    fun registrarUsuario() {
        val state = _uiState.value
        _uiErrors.value = RegistroErrores() // Resetea

        // 1. Validar campos
        var esValido = true

        if (state.nombre.isBlank()) {
            _uiErrors.update { it.copy(esErrorNombre = "El nombre es obligatorio") }
            esValido = false
        }

        if (state.correo.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(state.correo).matches()) {
            _uiErrors.update { it.copy(esErrorCorreo = "Debe ser un correo válido") }
            esValido = false
        }
        if (state.contrasena.length < 4) { // Validación simple
            _uiErrors.update { it.copy(esErrorContrasena = "Debe tener al menos 4 caracteres") }
            esValido = false
        }

        if (!esValido) {
            _mensaje.value = "Error: Revisa los campos."
            return
        }

        // Crear y Guardar Usuario
        viewModelScope.launch {
            val nuevoUsuario = Usuario(
                nombre = state.nombre,
                correo = state.correo,
                contrasena = state.contrasena
            )

            repository.insertar(nuevoUsuario)
            _mensaje.value = "¡Usuario registrado con éxito!"
            _registroExitoso.value = true // Avisa a la vista para que navegue
        }
    }
}