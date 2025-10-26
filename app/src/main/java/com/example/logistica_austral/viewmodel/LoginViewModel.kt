package com.example.logistica_austral.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logistica_austral.model.LoginErrores
import com.example.logistica_austral.model.LoginUIState
import com.example.logistica_austral.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UsuarioRepository) : ViewModel() {

    // Estados de la UI (correo, contraseña)
    private val _uiState = MutableStateFlow(LoginUIState())
    val uiState: StateFlow<LoginUIState> = _uiState.asStateFlow()

    //  Estado de Errores
    private val _uiErrors = MutableStateFlow(LoginErrores())
    val uiErrors: StateFlow<LoginErrores> = _uiErrors.asStateFlow()

    // Estado para el Mensaje
    private val _mensaje = MutableStateFlow("")
    val mensaje: StateFlow<String> = _mensaje.asStateFlow()

    // Para avisar a la Vista que el login fue exitoso
    private val _loginExitoso = MutableStateFlow(false)
    val loginExitoso: StateFlow<Boolean> = _loginExitoso.asStateFlow()


    // FUNCIONES ON-CHANGE

    fun onNombreChange(nombre: String) {
        _uiState.update { it.copy(nombre = nombre) }
    }

    fun onCorreoChange(correo: String) {
        _uiState.update { it.copy(correo = correo) }
        _uiErrors.update { it.copy(esErrorCorreo = null) } // Limpia el error
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(contrasena = password) }
        _uiErrors.update { it.copy(esErrorContrasena = null) }
    }


    // FUNCIÓN PRINCIPAL DE LOGIN
    fun iniciarSesion() {
        val state = _uiState.value
        _uiErrors.value = LoginErrores() // Resetea errores
        _mensaje.value = ""


        if (state.correo.isBlank()) {
            _uiErrors.update { it.copy(esErrorCorreo = "El correo no puede estar vacío") }
            return // Detiene la función si hay error
        }
        if (state.contrasena.isBlank()) {
            _uiErrors.update { it.copy(esErrorContrasena = "La contraseña no puede estar vacía") }
            return // Detiene la función si hay error
        }


        viewModelScope.launch {
            val usuarioEncontrado = repository.login(state.correo, state.contrasena)

            if (usuarioEncontrado != null) {
                _mensaje.value = "¡Bienvenido ${usuarioEncontrado.nombre}!"
                _loginExitoso.value = true
            } else {
                _mensaje.value = "Error: Correo o contraseña incorrectos."
            }
        }
    }
}