package com.example.milistaapp

sealed class Screen(val route: String) {
    object Lista : Screen("lista")
    object Nueva : Screen("nueva")
    object Configuracion : Screen("configuracion")
}
