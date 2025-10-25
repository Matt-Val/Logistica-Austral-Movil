package com.example.logistica_austral

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.logistica_austral.ui.theme.LogisticaAustralTheme
import com.example.logistica_austral.view.ExplorarFlotaScreen // actualizado nombre de pantalla

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LogisticaAustralTheme {
                ExplorarFlotaScreen()
            }
        }
    }
}
