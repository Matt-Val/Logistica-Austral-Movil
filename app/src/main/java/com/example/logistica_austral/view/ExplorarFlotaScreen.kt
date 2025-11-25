package com.example.logistica_austral.view

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.logistica_austral.model.Carrito
import com.example.logistica_austral.viewmodel.ExplorarViewModel
import com.example.logistica_austral.model.AppDatabase
import com.example.logistica_austral.repository.CamionRepository
import java.net.URLEncoder
import com.example.logistica_austral.R
import androidx.compose.foundation.layout.size
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class) // top app bar esta como experimental con material3, de esta forma nos permite usar experimentales de la libreria
@Composable // recordar: indica que esta funcion define una UI en compose
fun ExplorarFlotaScreen(nav: NavHostController) {
    val context = LocalContext.current // opobtiene el contexto actual (funciona para el toast)
    val carrito = remember { Carrito.getInstance(context) }

    // Construye Repository desde Room una sola vez
    val db = remember { AppDatabase.getDatabase(context) }
    val camionRepository = remember { CamionRepository(db.camionDao()) }

    val viewModel = remember { ExplorarViewModel(carrito, camionRepository) }

    val camiones by viewModel.camiones.collectAsState() // obtiene la lista en tiempo real
    val isLoading by viewModel.isLoadingCart.collectAsState()
    val error by viewModel.error.collectAsState()

    // efecto para mostrar toast de error una sola vez
    LaunchedEffect(error) {
        if (error != null) {
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }

    Scaffold( // estrucuta la pantalla con una "barra superior"
        topBar = {
            TopAppBar(
                title = { Text("Explorar Flota Usados", style = MaterialTheme.typography.titleLarge) },
                actions = {
                    IconButton(onClick = {nav.navigate("carrito")}) {
                        // de material3
                        Icon(Icons.Outlined.ShoppingCart, contentDescription = "Carrito")
                    }
                }
            )
        },
        floatingActionButton = {
            // boton flotante API whatsApp
            FloatingActionButton(
                onClick = {
                    abrirWhatsApp(
                        context,
                        viewModel.whatsappNumeroEmpresa,
                        viewModel.whatsappMensaje
                    )
                },
                containerColor = Color(0xFF25D366), // color del contenedor
                contentColor = Color.White
            ) {
                Image(
                    painter = painterResource(id = R.drawable.wsp),
                    contentDescription = "Contactar por WhatsApp",
                    modifier = Modifier.size(32.dp), // tamano de icono personalizado, (en caso de imagenes mas grandes)
                    contentScale = ContentScale.Fit
                )
            }
        }
    ) { inner -> // para evitar que el contenido quede debajo de la barra
        if (isLoading) {
            // si esta en cargando
            Box(
                modifier = Modifier.fillMaxSize().padding(inner), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            // si NO, esta cargando
            LazyVerticalGrid(
                columns = GridCells.Fixed(1), // 2 por pantalla (2 cards)
                modifier = Modifier
                    .fillMaxSize()
                    .padding(inner)
                    .padding(12.dp),
                contentPadding = PaddingValues(0.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = camiones,
                    key = { it.id } // cual es la key, su id
                ) { camion -> // para cada camion, 1 card
                    CamionCard(
                        camion = camion,
                        onAgregar = { // nombre de accion
                            viewModel.onAgregarACarrito(camion)
                            // el toast, es una notificacion nativa
                            Toast
                                .makeText(
                                    context,
                                    "${camion.marca} ${camion.modelo} agregado", // mensaje
                                    Toast.LENGTH_SHORT
                                )
                                .show() // la permite mostrar
                        },
                        modifier = Modifier // cada tarjeta ocupa el ancho de su celda
                    )
                }
            }
        }
    }
}

// utilidad para: abre WhatsApp con numero y mensaje prellenado
private fun abrirWhatsApp(context: android.content.Context, numeroTelefono: String, mensaje: String) {
    try {
        val urlMensaje = URLEncoder.encode(mensaje, "UTF-8")
        val uri = Uri.parse("https://api.whatsapp.com/send?phone=$numeroTelefono&text=$urlMensaje")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "No se encontro WhatsApp instalado", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(context, "No fue posible abrir WhatsApp", Toast.LENGTH_SHORT).show()
    }
}
