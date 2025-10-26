package com.example.logistica_austral.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.logistica_austral.viewmodel.CamionViewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.logistica_austral.R

// (Usamos @OptIn para el TopAppBar)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioCamionScreen(
    navController: NavController, // Lo usaremos en el Paso 5
    viewModel: CamionViewModel

) {
    // ESTADOS DEL VIEWMODEL
    val uiState by viewModel.uiState.collectAsState()
    val uiErrors by viewModel.uiErrors.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()


    Box(modifier = Modifier.fillMaxSize()) {
        // Agregamos la imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.fondo_login),
            contentDescription = null, // Decoración
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Scaffold(
            // Para que se vea la imagen
            containerColor  = Color.Transparent,
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(title = { Text("Registrar Nuevo Camión") })
            },
        ) { paddingValues ->
            // COLUMNA CON SCROLL
            // (Usamos verticalScroll porque el formulario es muy largo)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()) // Permite deslizar
            ) {

                // CAMPOS DE TEXTO (TEXTFIELDS)

                // CAMPO PATENTE
                OutlinedTextField(
                    value = uiState.patente,
                    onValueChange = { viewModel.onPatenteChange(it) },
                    label = { Text("Patente") },
                    placeholder = { Text("EJ: XX-XX-XX") },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color(0xFF0F9D58),
                        unfocusedTextColor = Color(0xFF0F9D58),
                    ),
                    isError = uiErrors.esErrorPatente != null, // Revisa si no es nulo
                    supportingText = {
                        if (uiErrors.esErrorPatente != null) {
                            Text(uiErrors.esErrorPatente!!, color = Color.White)
                            // Revisa si no es nulo
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // CAMPO MARCA
                OutlinedTextField(
                    value = uiState.marca,
                    onValueChange = { viewModel.onMarcaChange(it) },
                    label = { Text("Marca") },
                    placeholder = {Text("EJ: Scania") },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color(0xFF0F9D58),
                        unfocusedTextColor = Color(0xFF0F9D58),
                    ),
                    isError = uiErrors.esErrorMarca != null,
                    supportingText = {
                        if (uiErrors.esErrorMarca != null) {
                            Text(uiErrors.esErrorMarca!!, color = Color.White)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // CAMPO MODELO
                OutlinedTextField(
                    value = uiState.modelo,
                    onValueChange = { viewModel.onModeloChange(it) },
                    label = { Text("Modelo") },
                    placeholder = {Text("EJ: V8") },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color(0xFF0F9D58),
                        unfocusedTextColor = Color(0xFF0F9D58),
                    ),
                    isError = uiErrors.esErrorModelo != null,
                    supportingText = {
                        if (uiErrors.esErrorModelo != null) {
                            Text(uiErrors.esErrorModelo!!, color = Color.White)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // CAMPO AÑO
                OutlinedTextField(
                    value = uiState.annio,
                    onValueChange = { viewModel.onAnnioChange(it) },
                    label = { Text("Año") },
                    placeholder = { Text("EJ: 1990") },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color(0xFF0F9D58),
                        unfocusedTextColor = Color(0xFF0F9D58),
                    ),
                    isError = uiErrors.esErrorAnnio != null,
                    supportingText = {
                        if (uiErrors.esErrorAnnio != null) {
                            Text(uiErrors.esErrorAnnio!!, color = Color.White)
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // CAMPO TIPO
                OutlinedTextField(
                    value = uiState.tipo,
                    onValueChange = { viewModel.onTipoChange(it) },
                    label = { Text("Tipo") },
                    placeholder = { Text("EJ: Tolva/o Tracto") },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color(0xFF0F9D58),
                        unfocusedTextColor = Color(0xFF0F9D58),
                    ),
                    isError = uiErrors.esErrorTipo != null,
                    supportingText = {
                        if (uiErrors.esErrorTipo != null) {
                            Text(uiErrors.esErrorTipo!!, color = Color.White)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // CAMPO CAPACIDAD
                OutlinedTextField(
                    value = uiState.capacidad,
                    onValueChange = { viewModel.onCapacidadChange(it) },
                    label = { Text("Capacidad  (en Toneladas)") },
                    placeholder = { Text("EJ: 1000") },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color(0xFF0F9D58),
                        unfocusedTextColor = Color(0xFF0F9D58),
                    ),
                    isError = uiErrors.esErrorCapacidad != null,
                    supportingText = {
                        if (uiErrors.esErrorCapacidad != null) {
                            Text(uiErrors.esErrorCapacidad!!, color = Color.White)
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // CAMPO ESTADO
                OutlinedTextField(
                    value = uiState.estado,
                    onValueChange = { viewModel.onEstadoChange(it) },
                    label = { Text("Estado") },
                    placeholder = {Text("EJ: Operativo") },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color(0xFF0F9D58),
                        unfocusedTextColor = Color(0xFF0F9D58),
                    ),
                    isError = uiErrors.esErrorEstado != null,
                    supportingText = {
                        if (uiErrors.esErrorEstado != null) {
                            Text(uiErrors.esErrorEstado!!, color = Color.White)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // CAMPO DESCRIPCIÓN
                OutlinedTextField(
                    value = uiState.descripcion,
                    onValueChange = { viewModel.onDescripcionChange(it) },
                    label = { Text("Descripción") },
                    placeholder= {Text("EJ: Camión de carga") },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color(0xFF0F9D58),
                        unfocusedTextColor = Color(0xFF0F9D58),
                    ),
                    isError = uiErrors.esErrorDescripcion != null,
                    supportingText = {
                        if (uiErrors.esErrorDescripcion != null) {
                            Text(uiErrors.esErrorDescripcion!!, color = Color.White)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // CAMPO TRACCIÓN
                OutlinedTextField(
                    value = uiState.traccion,
                    onValueChange = { viewModel.onTraccionChange(it) },
                    label = { Text("Tracción") },
                    placeholder = { Text("EJ: 4x4") },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color(0xFF0F9D58),
                        unfocusedTextColor = Color(0xFF0F9D58),
                    ),
                    isError = uiErrors.esErrorTraccion != null,
                    supportingText = {
                        if (uiErrors.esErrorTraccion != null) {
                            Text(uiErrors.esErrorTraccion!!, color = Color.White)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                // SWITCH PARA DISPONIBILIDAD
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "¿Está Disponible?", color= Color.White, modifier = Modifier.weight(1f))
                    Switch(
                        checked = uiState.disponibilidad,
                        onCheckedChange = { viewModel.onDisponibilidadChange(it) }
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))

                // BOTÓN DE ACCIÓN Y MENSAJE
                Button(
                    onClick = { viewModel.registrarCamion() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Registrar Camión",
                        color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Muestra el mensaje de éxito o error general
                Text(text = mensaje, color = Color.White)
            }
        }
    }
}