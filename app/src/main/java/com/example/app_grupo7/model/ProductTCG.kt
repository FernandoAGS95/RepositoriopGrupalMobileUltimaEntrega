package com.example.app_grupo7.model

data class ProductTCG(
    val id: Long? = null,
    val nombre: String = "",
    val descripcion: String = "",
    val precio: Int = 0,
    val imagen: String = "",
    val hover: String? = null,
    val oferta: String? = null
){
    // Construir la URL completa para la imagen principal
    val imagenUrlCompleta: String
        get() = "http://13.219.43.161:8080$imagen"

    // Construir la URL completa para el hover
    val hoverUrlCompleta: String?
        get() = hover?.let { "http://13.219.43.161:8080$it" }
}