package com.example.logistica_austral.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.logistica_austral.R
import com.example.logistica_austral.viewmodel.LoginViewModel
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.graphics.Color


@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel
) {
    // estados del ViewModel
    val uiState by viewModel.uiState.collectAsState()
    val uiErrors by viewModel.uiErrors.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()
    val loginExitoso by viewModel.loginExitoso.collectAsState()

    // NAVEGACIÓN AUTOMÁTICA
    // Si el ViewModel la pone en 'true', este bloque se ejecuta.
    LaunchedEffect(loginExitoso) {
        if (loginExitoso) {
            // Navega a la pantalla principal
            navController.navigate("homeScreen") {
                popUpTo("loginScreen") { inclusive = true }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.fondo_login),
            contentDescription = null, // Decoración
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Scaffold (
            containerColor  = Color.Transparent,
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center, // Centra verticalmente
                horizontalAlignment = Alignment.CenterHorizontally // Centra horizontalmente
            ) {
                Text("Iniciar Sesión", style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
                    color = Color.White)
                Spacer(modifier = Modifier.height(24.dp))

                //  CAMPO CORREO
                OutlinedTextField(
                    value = uiState.correo,
                    onValueChange = { viewModel.onCorreoChange(it) },
                    label = { Text("Correo Electrónico") },
                    placeholder = { Text("tu@correo.com") },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color(0xFF0F9D58),
                        unfocusedTextColor = Color(0xFF0F9D58),
                    ),
                    isError = uiErrors.esErrorCorreo != null, // Muestra error si no es null
                    supportingText = {
                        if (uiErrors.esErrorCorreo != null) {
                            Text(uiErrors.esErrorCorreo!!) // Muestra el mensaje de error
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // CAMPO CONTRASEÑA (Conectado al ViewModel)
                OutlinedTextField(
                    value = uiState.contrasena,
                    onValueChange = { viewModel.onPasswordChange(it) },
                    label = { Text("Contraseña") },
                    placeholder= {Text("Ingrese su Contraseña")},
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color(0xFF0F9D58),
                        unfocusedTextColor = Color(0xFF0F9D58),
                    ),
                    isError = uiErrors.esErrorContrasena != null,
                    supportingText = {
                        if (uiErrors.esErrorContrasena != null) {
                            Text(uiErrors.esErrorContrasena!!, color = Color.White)
                        }
                    },
                    visualTransformation = PasswordVisualTransformation(), // Oculta la contraseña
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))

                // BOTÓN INICIAR SESIÓN
                Button(
                    onClick = {viewModel.iniciarSesion() }, // Llama a la función en el ViewModel
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ingresar")
                }

                TextButton(
                    onClick = { navController.navigate("registroScreen") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("¿No tienes cuenta? Regístrate aquí")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Mensaje de éxito o error
                Text(text = mensaje, color = Color.White)
            }
        }
    }
}