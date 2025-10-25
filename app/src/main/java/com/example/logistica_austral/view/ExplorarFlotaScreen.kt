package com.example.logistica_austral.view

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.logistica_austral.viewmodel.ExplorarViewModel

@OptIn(ExperimentalMaterial3Api::class) // top app bar esta como experimental con material3, de esta forma nos permite usar experimentales de la libreria
@Composable // recordar: indica que esta funcion define una UI en compose
fun ExplorarFlotaScreen(
    viewModel: ExplorarViewModel = remember { ExplorarViewModel() }
) {
    val context = LocalContext.current // opobtiene el contexto actual (funciona para el toast)
    val camiones by viewModel.camiones.collectAsState() // obtiene la lista en tiempo real


    Scaffold( // estrucuta la pantalla con una "barra superior"
        topBar = {
            TopAppBar(
                title = { Text("Explorar Flota Usados", style = MaterialTheme.typography.titleLarge) }
            )
        }
    ) { inner -> // para evitar que el contenido quede debajo de la barra
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