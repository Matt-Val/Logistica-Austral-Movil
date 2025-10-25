package com.example.logistica_austral

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.logistica_austral.ui.theme.LogisticaAustralTheme
import com.example.logistica_austral.view.CarritoScreen
import com.example.logistica_austral.view.ExplorarFlotaScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LogisticaAustralTheme {
                //ExplorarFlotaScreen() //
                AppNav()
            }
        }
    }
}

@Composable
private fun AppNav() {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = "explorar") { // este cambia despuess!
        // rutas:
        composable("explorar") { ExplorarFlotaScreen(nav) }
        composable("carrito") { CarritoScreen(nav) }
    }
}

@Composable
private fun PreviewApp() {
    LogisticaAustralTheme { AppNav() }
}
