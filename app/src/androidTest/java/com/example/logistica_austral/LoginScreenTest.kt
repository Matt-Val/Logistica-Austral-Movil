package com.example.logistica_austral

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import com.example.logistica_austral.repository.UsuarioRepository
import com.example.logistica_austral.view.LoginScreen
import com.example.logistica_austral.viewmodel.LoginViewModel
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test


class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun elementos_de_login_se_muestran_correctamente() {
        // Mock simple del repositorio para que el viewModel no falle al instanciarse
        val mockRepo = mockk<UsuarioRepository>(relaxed = true)
        val viewModel = LoginViewModel(mockRepo)

        // Cargamos la pantalla LoginScreen en el entorno de prueba
        composeTestRule.setContent {
            val navController = rememberNavController()
            LoginScreen(navController = navController, viewModel = viewModel)
        }

        // Validaciones

        // 1. Verificar que el titulo Iniciar Sesion está visible
        composeTestRule.onNodeWithText("Iniciar Sesión").assertIsDisplayed()

        // 2. Verificar que el botón Ingresar está visible
        composeTestRule.onNodeWithText("Ingresar").assertIsDisplayed()

        // 3. Verificar el texto de registro
        composeTestRule.onNodeWithText("¿No tienes cuenta? Regístrate aquí").assertIsDisplayed()
    }

}