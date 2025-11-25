package com.example.logistica_austral.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.logistica_austral.R
import com.example.logistica_austral.util.ImagenCompressor
import com.example.logistica_austral.viewmodel.CamionViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
    val context = LocalContext.current

    // --- Lanzadores para galería y cámara ---
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.onImagenUriChange(uri)
    }

    var cameraTempUri by remember { mutableStateOf<Uri?>(null) }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            viewModel.onImagenUriChange(cameraTempUri)
        }
    }

    val requestCameraPermission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val uri = createImageUri(context)
            cameraTempUri = uri
            takePictureLauncher.launch(uri)
        }
    }

    // --- UI ---
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
                // --- Vista previa de imagen ---
                PreviewImagenCamion(uiState.imagenUri)
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { pickImageLauncher.launch("image/*") }, modifier = Modifier.weight(1f)) {
                        Text("Galería")
                    }
                    Button(onClick = {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            val uri = createImageUri(context)
                            cameraTempUri = uri
                            takePictureLauncher.launch(uri)
                        } else {
                            requestCameraPermission.launch(Manifest.permission.CAMERA)
                        }
                    }, modifier = Modifier.weight(1f)) {
                        Text("Cámara")
                    }
                }
                Spacer(Modifier.height(24.dp))

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
                Spacer(modifier = Modifier.height(8.dp))

                // CAMPO: Precio
                OutlinedTextField(
                    value = uiState.precio,
                    onValueChange = { viewModel.onPrecioChange(it) },
                    label = { Text("Precio (CLP)") },
                    placeholder = { Text("EJ: 5000000") },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color(0xFF0F9D58),
                        unfocusedTextColor = Color(0xFF0F9D58),
                    ),
                    isError = uiErrors.esErrorPrecio != null,
                    supportingText = {
                        if (uiErrors.esErrorPrecio != null) {
                            Text(uiErrors.esErrorPrecio!!, color = Color.White)
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

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

                Button(
                    onClick = {
                        val uri = uiState.imagenUri
                        // COMPRESIÓN: si existe Uri, se comprime y redimensiona (max 720px) antes de enviar
                        val file = uri?.let { ImagenCompressor.comprimir(context, it) }
                        viewModel.registrarCamionRemotoConImagen(file)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { Text(text = "Registrar nuevo camión", color = Color.White) }

                Spacer(modifier = Modifier.height(16.dp))
                Text(text = mensaje, color = Color.White)
            }
        }
    }
}

@Composable
private fun PreviewImagenCamion(uri: Uri?) {
    if (uri != null) {
        Image(
            painter = rememberAsyncImagePainter(model = uri),
            contentDescription = "Imagen del camión",
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            contentScale = ContentScale.Crop
        )
    } else {
        // Placeholder simple
        Image(
            painter = painterResource(id = R.drawable.camion),
            contentDescription = "Imagen por defecto",
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            contentScale = ContentScale.Crop
        )
    }
}

private fun createImageUri(context: Context): Uri {
    val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val file = File.createTempFile("JPEG_${timestamp}_", ".jpg", storageDir)
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )
}
