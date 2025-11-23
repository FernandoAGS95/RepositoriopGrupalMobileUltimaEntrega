package com.example.app_grupo7.navigation

sealed class NavRoutes(val route: String) {
    data object Home : NavRoutes("home")
    data object Catalogo : NavRoutes("catalogo")
    data object Registro : NavRoutes("registro")
    data object Resumen : NavRoutes("resumen")
    data object ProductosTCG : NavRoutes("productosTCG")
}
