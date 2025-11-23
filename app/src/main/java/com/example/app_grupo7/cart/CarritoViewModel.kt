package com.example.app_grupo7.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_grupo7.cart.CartItem
import com.example.app_grupo7.cart.CartStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CarritoViewModel(private val store: CartStore) : ViewModel() {

    val items: StateFlow<List<CartItem>> =
        store.itemsFlow.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val total: StateFlow<Int> =
        store.totalFlow.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun addFromPerfume(id: String, nombre: String, precio: Int, imageRes: Int?) {
        viewModelScope.launch {
            store.addOrIncrement(CartItem(id, nombre, precio, imageRes,imageUri = null, 1,))
        }
    }

    fun setQty(perfumeId: String, qty: Int) {
        viewModelScope.launch { store.setQuantity(perfumeId, qty) }
    }

    fun remove(perfumeId: String) {
        viewModelScope.launch { store.remove(perfumeId) }
    }

    fun clear() {
        viewModelScope.launch { store.clear() }
    }
    fun addOrIncrement(perfumeId: String, nombre: String, precio: Int, imageRes: Int?,imageUri: String?
    ) {
        viewModelScope.launch {
            store.addOrIncrement(
                CartItem(
                    perfumeId = perfumeId,
                    nombre = nombre,
                    precio = precio,
                    imageRes = imageRes,
                    imageUri = imageUri,
                    quantity = 1
                )
            )
        }
    }
}
