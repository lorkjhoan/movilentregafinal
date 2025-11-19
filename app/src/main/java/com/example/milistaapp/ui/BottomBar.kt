package com.example.milistaapp.ui

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.milistaapp.Screen
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomBar(navController: NavHostController) {

    val screens = listOf(
        Screen.Lista,
        Screen.Nueva,
        Screen.Configuracion
    )

    NavigationBar {
        val navBackStackEntry = navController.currentBackStackEntryAsState().value
        val currentRoute = navBackStackEntry?.destination?.route

        screens.forEach { screen ->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(Screen.Lista.route)
                        launchSingleTop = true
                    }
                },
                icon = {
                    when (screen) {
                        Screen.Lista -> Icon(Icons.Filled.List, contentDescription = "Lista")
                        Screen.Nueva -> Icon(Icons.Filled.Add, contentDescription = "Nueva")
                        Screen.Configuracion -> Icon(Icons.Filled.Settings, contentDescription = "Configuración")
                    }
                },
                label = {
                    Text(
                        when (screen) {
                            Screen.Lista -> "Lista"
                            Screen.Nueva -> "Nueva"
                            Screen.Configuracion -> "Config"
                        }
                    )
                }
            )
        }
    }
}
