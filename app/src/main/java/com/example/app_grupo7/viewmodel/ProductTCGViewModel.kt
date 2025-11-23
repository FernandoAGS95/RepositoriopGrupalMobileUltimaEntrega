package com.example.app_grupo7.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_grupo7.model.ProductTCG
import com.example.app_grupo7.repository.ProductTCGRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductTCGViewModel(
    private val repo: ProductTCGRepository = ProductTCGRepository()
) : ViewModel() {



    private val _products = MutableStateFlow<List<ProductTCG>>(emptyList())
    val products: StateFlow<List<ProductTCG>> = _products

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    fun getProductById(id: Long) {
        viewModelScope.launch {
            _loading.value = true
            val result = repo.getProductById(id)
            _loading.value = false

            result.onSuccess {
                _products.value = listOf(it) // guarda solo el producto recibido
            }.onFailure {
                _error.value = it.message
            }
        }
    }
    fun loadProducts() {
        viewModelScope.launch {
            _loading.value = true
            val result = repo.getAllProducts()
            _loading.value = false

            result.onSuccess {
                _products.value = it
            }.onFailure {
                _error.value = it.message
            }
        }
    }
}