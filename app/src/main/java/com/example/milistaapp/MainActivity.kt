package com.example.milistaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.compose.*
import com.example.milistaapp.ui.BottomBar
import com.example.milistaapp.ui.lista.ListaScreen
import com.example.milistaapp.ui.nueva.NuevaTareaScreen
import com.example.milistaapp.ui.configuracion.ConfigScreen
import com.example.milistaapp.ui.theme.MiListaAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MiListaAppTheme {

                val navController = rememberNavController()

                Scaffold(
                    bottomBar = {
                        BottomBar(navController)
                    }
                ) { paddingValues ->

                    NavHost(
                        navController = navController,
                        startDestination = Screen.Lista.route,
                        modifier = androidx.compose.ui.Modifier.padding(paddingValues)
                    ) {

                        composable(Screen.Lista.route) {
                            ListaScreen()
                        }
                        composable(Screen.Nueva.route) {
                            NuevaTareaScreen(navController)
                        }
                        composable(Screen.Configuracion.route) {
                            ConfigScreen()
                        }
                    }
                }
            }
        }
    }
}
