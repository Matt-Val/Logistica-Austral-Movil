package com.example.logistica_austral.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

import com.example.logistica_austral.viewmodel.RegistroViewModel
import com.example.logistica_austral.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.layout.ContentScale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    navController: NavController,
    viewModel: RegistroViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val uiErrors by viewModel.uiErrors.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()
    val registroExitoso by viewModel.registroExitoso.collectAsState()

    // Navegación automática: Si el registro es exitoso,
    // vuelve a la pantalla de Login
    LaunchedEffect(registroExitoso) {
        if (registroExitoso) {
            // Regresa a la pantalla anterior (Login)
            navController.popBackStack()
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
        ){ paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Crear Nueva Cuenta",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White)


                Spacer(modifier = Modifier.height(24.dp))

                // CAMPO NOMBRE
                OutlinedTextField(
                    value = uiState.nombre,
                    onValueChange = { viewModel.onNombreChange(it) },
                    label = { Text("Nombre Completo") },
                    placeholder = { Text("Nombre y Apellido") },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color(0xFF0F9D58),
                        unfocusedTextColor = Color(0xFF0F9D58),
                    ),
                    isError = uiErrors.esErrorNombre != null,
                    supportingText = {
                        if (uiErrors.esErrorNombre != null) {
                            Text(uiErrors.esErrorNombre!!)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(8.dp))

                // CAMPO CORREO
                OutlinedTextField(
                    value = uiState.correo,
                    onValueChange = { viewModel.onCorreoChange(it) },
                    label = { Text("Correo Electrónico")},
                    placeholder = {Text("tucorreo@correo.com") },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color(0xFF0F9D58),
                        unfocusedTextColor = Color(0xFF0F9D58),
                    ),
                    isError = uiErrors.esErrorCorreo != null,
                    supportingText = {
                        if (uiErrors.esErrorCorreo != null) {
                            Text(uiErrors.esErrorCorreo!!)
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // CAMPO CONTRASEÑA
                OutlinedTextField(
                    value = uiState.contrasena,
                    onValueChange = { viewModel.onPasswordChange(it) },
                    label = { Text("Contraseña (mín. 4 caracteres)") },
                    placeholder = { Text("Ingrese su contraseña") },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color(0xFF0F9D58),
                        unfocusedTextColor = Color(0xFF0F9D58),
                    ),
                    isError = uiErrors.esErrorContrasena != null,
                    supportingText = {
                        if (uiErrors.esErrorContrasena != null) {
                            Text(uiErrors.esErrorContrasena!!)
                        }
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))

                // BOTÓN REGISTRAR
                Button(
                    onClick = { viewModel.registrarUsuario() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Registrarme")
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(text = mensaje, color = Color.White) // Mensaje de éxito o error
                }
            }
        }
    }