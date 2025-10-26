package com.example.logistica_austral.view

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
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
    val carritoItems by vm.camionesEnCarrito.collectAsState()

    // para el total del carrito: uso annio como precio simulado (annio es string lo convierto a int)
    val total = remember(carritoItems) { carritoItems.sumOf { it.precio } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Carrito", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    // boton para volver atras, con iconos nativo material3
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        bottomBar = {
            // barra inferior fija que contiene el boton principal de accion de compra
            BottomAppBar {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                ) {
                    // total mostrado a la izquierda
                    Text(
                        text = "Total: $ $total",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.weight(1f)) // este es un separador flexible para empujar el boton a la derecha

                    Button(
                        onClick = {
                            // SIMULACION de compra muestro un toast
                            Toast.makeText(context, "Compra realizada", Toast.LENGTH_LONG).show()
                        }
                    ) {
                        Text("Realizar compra")
                    }
                }
            }
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
            items(carritoItems) { camion ->
                CamionCard(
                    camion = camion,
                    onAgregar = {},
                    showAgregar = false,
                    onQuitar = { vm.onQuitarDelCarrito(camion) }, // agrego el boton de quitar, reutilizando la misma card
                    showQuitar = true
                )
            }
        }
    }
}
