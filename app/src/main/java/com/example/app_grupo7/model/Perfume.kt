package com.example.app_grupo7.model
import androidx.annotation.DrawableRes
data class Perfume(
    val id: String,
    val nombre: String,
    val marca: String,
    val precio: Int,
    val ml: Int,
    val descripcion: String,
    @DrawableRes val imageRes: Int? = null
)
