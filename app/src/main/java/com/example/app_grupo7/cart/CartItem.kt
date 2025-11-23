package com.example.app_grupo7.cart

data class CartItem(
    val perfumeId: String,
    val nombre: String,
    val precio: Int,
    val imageRes: Int? = null,
    val imageUri: String? = null,
    val quantity: Int = 1
)