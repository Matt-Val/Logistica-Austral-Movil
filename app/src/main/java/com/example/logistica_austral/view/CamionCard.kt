package com.example.logistica_austral.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.logistica_austral.R
import com.example.logistica_austral.model.Camion
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete

// esta tarjeta la quiero reutilizar, para no tener que escribir producto x producto, very helpful
@Composable
fun CamionCard(
    camion: Camion,
    onAgregar: () -> Unit, // para boton de agregar al carrito
    modifier: Modifier = Modifier,
    showAgregar: Boolean = true,
    onQuitar: () -> Unit = {},  // para boton quitar de carrito
    showQuitar: Boolean = false
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // IMAGEN SUPERIOR
            Image(
                painter = painterResource(id = R.drawable.camion), // drawable en comun para probar
                contentDescription = null, // decorativa
                contentScale = ContentScale.Crop, // asegura que la card mantenga tamano consistente recortando la imagen si es necesario
                modifier = Modifier.fillMaxWidth()
                    .height(140.dp) // altura fija para uniformidad entre cards
            )

            Column(Modifier.padding(12.dp)) {
                Text(
                    text = "$ ${camion.annio}", // simula precio
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${camion.marca} ${camion.modelo}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.height(4.dp)) // util

                Text(
                    text = "Tipo: ${camion.tipo} \nTracción: ${camion.traccion}\n${camion.descripcion}\nAño: ${camion.annio}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (showAgregar) {
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = onAgregar,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Agregar a carrito")
                    }
                } else if (showQuitar) {
                    Spacer(Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        IconButton(onClick = onQuitar) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = "Quitar del carrito"
                            )
                        }
                    }
                }
            }
        }
    }
}