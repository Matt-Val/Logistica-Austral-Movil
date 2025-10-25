package com.example.logistica_austral.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.logistica_austral.model.Carrito
import com.example.logistica_austral.viewmodel.CarritoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(nav: NavHostController) {
    val context = LocalContext.current
    val carrito = remember { Carrito.getInstance(context) }
    val vm = remember { CarritoViewModel(carrito) }
    val items by vm.camionesEnCarrito.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Carrito", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    // boton para volver atras, con iconos nativo material3
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.Outlined.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { inner ->
        // este es el formato para la columna que tendra los items
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 12.dp)
        ) {
            items(items) { camion ->
                // reutilizo la misma card
                CamionCard(camion = camion, onAgregar = {}, showAgregar = false)
            }
        }
    }
}
