package com.example.logistica_austral.view

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.logistica_austral.R
import com.example.logistica_austral.data.remote.NetworkConfig
import com.example.logistica_austral.viewmodel.EditarCamionViewModel
import androidx.core.net.toUri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarCamionScreen(
    navController: NavController,
    viewModel: EditarCamionViewModel,
    camionId: Int
) {
    val uiState by viewModel.uiState.collectAsState()
    val uiErrors by viewModel.uiErrors.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    val context = androidx.compose.ui.platform.LocalContext.current

    LaunchedEffect(camionId) { viewModel.cargar(camionId) }

    // Mostrar toast cuando mensaje cambie
    LaunchedEffect(mensaje) {
        if (mensaje.isNotBlank()) {
            Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.fondo_login),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("Editar Camión") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                        }
                    }
                )
            }
        ) { inner ->
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(inner),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(inner)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Imagen remota: si es relativa completar con base URL
                    val imagenFinal: Uri? = uiState.imagenUri?.let { uri ->
                        val s = uri.toString()
                        if (s.startsWith("http")) uri else (NetworkConfig.SERVER_BASE_URL + s).toUri()
                    }
                    PreviewImagen(imagenFinal)
                    Spacer(Modifier.height(24.dp))

                    // Config colores reutilizables
                    val errorColor = Color.White
                    val textoCampo = Color(0xFF0F9D58)

                    // Patente
                    OutlinedTextField(
                        value = uiState.patente,
                        onValueChange = { viewModel.onPatenteChange(it) },
                        label = { Text("Patente") },
                        placeholder = { Text("EJ: XX-XX-XX") },
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = textoCampo,
                            unfocusedTextColor = textoCampo
                        ),
                        isError = uiErrors.esErrorPatente != null,
                        supportingText = { uiErrors.esErrorPatente?.let { Text(it, color = errorColor) } },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    // Marca
                    OutlinedTextField(
                        value = uiState.marca,
                        onValueChange = { viewModel.onMarcaChange(it) },
                        label = { Text("Marca") },
                        placeholder = { Text("EJ: Scania") },
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = textoCampo,
                            unfocusedTextColor = textoCampo
                        ),
                        isError = uiErrors.esErrorMarca != null,
                        supportingText = { uiErrors.esErrorMarca?.let { Text(it, color = errorColor) } },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    // Modelo
                    OutlinedTextField(
                        value = uiState.modelo,
                        onValueChange = { viewModel.onModeloChange(it) },
                        label = { Text("Modelo") },
                        placeholder = { Text("EJ: V8") },
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = textoCampo,
                            unfocusedTextColor = textoCampo
                        ),
                        isError = uiErrors.esErrorModelo != null,
                        supportingText = { uiErrors.esErrorModelo?.let { Text(it, color = errorColor) } },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    // Año
                    OutlinedTextField(
                        value = uiState.annio,
                        onValueChange = { viewModel.onAnnioChange(it) },
                        label = { Text("Año") },
                        placeholder = { Text("EJ: 1990") },
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = textoCampo,
                            unfocusedTextColor = textoCampo
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = uiErrors.esErrorAnnio != null,
                        supportingText = { uiErrors.esErrorAnnio?.let { Text(it, color = errorColor) } },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    // Tipo
                    OutlinedTextField(
                        value = uiState.tipo,
                        onValueChange = { viewModel.onTipoChange(it) },
                        label = { Text("Tipo") },
                        placeholder = { Text("EJ: Tolva o Tracto") },
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = textoCampo,
                            unfocusedTextColor = textoCampo
                        ),
                        isError = uiErrors.esErrorTipo != null,
                        supportingText = { uiErrors.esErrorTipo?.let { Text(it, color = errorColor) } },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    // Capacidad
                    OutlinedTextField(
                        value = uiState.capacidad,
                        onValueChange = { viewModel.onCapacidadChange(it) },
                        label = { Text("Capacidad (Toneladas)") },
                        placeholder = { Text("EJ: 1000") },
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = textoCampo,
                            unfocusedTextColor = textoCampo
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = uiErrors.esErrorCapacidad != null,
                        supportingText = { uiErrors.esErrorCapacidad?.let { Text(it, color = errorColor) } },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    // Estado
                    OutlinedTextField(
                        value = uiState.estado,
                        onValueChange = { viewModel.onEstadoChange(it) },
                        label = { Text("Estado") },
                        placeholder = { Text("EJ: Operativo") },
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = textoCampo,
                            unfocusedTextColor = textoCampo
                        ),
                        isError = uiErrors.esErrorEstado != null,
                        supportingText = { uiErrors.esErrorEstado?.let { Text(it, color = errorColor) } },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    // Descripcion
                    OutlinedTextField(
                        value = uiState.descripcion,
                        onValueChange = { viewModel.onDescripcionChange(it) },
                        label = { Text("Descripción") },
                        placeholder = { Text("EJ: Camión de carga") },
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = textoCampo,
                            unfocusedTextColor = textoCampo
                        ),
                        isError = uiErrors.esErrorDescripcion != null,
                        supportingText = { uiErrors.esErrorDescripcion?.let { Text(it, color = errorColor) } },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    // Traccion
                    OutlinedTextField(
                        value = uiState.traccion,
                        onValueChange = { viewModel.onTraccionChange(it) },
                        label = { Text("Tracción") },
                        placeholder = { Text("EJ: 4x4") },
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = textoCampo,
                            unfocusedTextColor = textoCampo
                        ),
                        isError = uiErrors.esErrorTraccion != null,
                        supportingText = { uiErrors.esErrorTraccion?.let { Text(it, color = errorColor) } },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    // Precio
                    OutlinedTextField(
                        value = uiState.precio,
                        onValueChange = { viewModel.onPrecioChange(it) },
                        label = { Text("Precio (CLP)") },
                        placeholder = { Text("EJ: 5000000") },
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = textoCampo,
                            unfocusedTextColor = textoCampo
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = uiErrors.esErrorPrecio != null,
                        supportingText = { uiErrors.esErrorPrecio?.let { Text(it, color = errorColor) } },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("¿Está Disponible?", color = Color.White, modifier = Modifier.weight(1f))
                        Switch(checked = uiState.disponibilidad, onCheckedChange = { viewModel.onDisponibilidadChange(it) })
                    }
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.guardarCambios() },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isSaving
                    ) { Text(if (isSaving) "Guardando..." else "Guardar Cambios", color = Color.White) }
                    if (isSaving) {
                        Spacer(Modifier.height(12.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    if (mensaje.isNotBlank()) {
                        Text(mensaje, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
private fun PreviewImagen(uri: Uri?) {
    if (uri != null) {
        Image(
            painter = rememberAsyncImagePainter(model = uri),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            contentScale = ContentScale.Crop
        )
    } else {
        Image(
            painter = painterResource(id = R.drawable.camion),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            contentScale = ContentScale.Crop
        )
    }
}
