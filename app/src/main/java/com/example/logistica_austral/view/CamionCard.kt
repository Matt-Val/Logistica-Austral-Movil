package com.example.logistica_austral.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.logistica_austral.model.Camion

// esta tarjeta la quiero reutilizar, para no tener que escribir producto x producto, very helpful
@Composable
fun CamionCard(
    camion: Camion,
    onAgregar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(
                text = "${camion.marca} ${camion.modelo}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(4.dp)) // util

            Text(
                text = "Tipo: ${camion.tipo}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(12.dp))


            Button(
                onClick = onAgregar,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agregar a carrito")
            }
        }
    }
}