package com.example.logistica_austral

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// --- IMPORTACIONES DEL MODELO ---
import com.example.logistica_austral.model.AppDatabase
import com.example.logistica_austral.repository.CamionRepository
import com.example.logistica_austral.repository.UsuarioRepository

// --- IMPORTACIONES DE LAS VISTAS (PANTALLAS) ---
import com.example.logistica_austral.view.FormularioCamionScreen
import com.example.logistica_austral.view.HomeScreen
import com.example.logistica_austral.view.LoginScreen
import com.example.logistica_austral.view.RegistroScreen

// --- IMPORTACIONES DE LOS VIEWMODELS ---
import com.example.logistica_austral.viewmodel.CamionViewModel
import com.example.logistica_austral.viewmodel.LoginViewModel
import com.example.logistica_austral.viewmodel.RegistroViewModel

// --- IMPORTACIÓN DEL TEMA ---
import com.example.logistica_austral.ui.theme.LogisticaAustralTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LogisticaAustralTheme {

                // 1. INICIALIZACIÓN DE COMPONENTES
                val navController = rememberNavController()
                val context = LocalContext.current

                // Inicializa la Base de Datos (que conoce a Usuario y Camion)
                val db = remember { AppDatabase.getDatabase(context) }

                // Inicializa AMBOS repositorios
                val usuarioRepository = remember { UsuarioRepository(db.usuarioDao()) }
                val camionRepository = remember { CamionRepository(db.camionDao()) }

                // Inicializa TODOS los ViewModels
                val loginViewModel: LoginViewModel = viewModel {
                    LoginViewModel(usuarioRepository)
                }
                val registroViewModel: RegistroViewModel = viewModel {
                    RegistroViewModel(usuarioRepository)
                }
                val camionViewModel: CamionViewModel = viewModel {
                    CamionViewModel(camionRepository)
                }

                // CONFIGURACIÓN DE NAVEGACIÓN (NavHost)

                NavHost(
                    navController = navController,
                    startDestination = "loginScreen" // La app empieza en el Login
                ) {

                    // Ruta para la pantalla de Login
                    composable("loginScreen") {
                        LoginScreen(
                            navController = navController,
                            viewModel = loginViewModel
                        )
                    }

                    // Ruta para la pantalla de Registro
                    composable("registroScreen") {
                        RegistroScreen(
                            navController = navController,
                            viewModel = registroViewModel
                        )
                    }

                    // Ruta para la pantalla Principal (después del login)
                    composable("homeScreen") {
                        HomeScreen(navController = navController)
                    }

                    // Ruta para el formulario de Camiones
                    composable("formularioCamion") {
                        FormularioCamionScreen(
                            navController = navController,
                            viewModel = camionViewModel
                        )
                    }
                }
            }
        }
    }
}