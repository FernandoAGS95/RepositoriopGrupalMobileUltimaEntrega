package com.example.app_grupo7.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.app_grupo7.model.Perfume
import com.example.app_grupo7.repository.PerfumeRepository

class PerfumeViewModel : ViewModel() {
    private val _catalogo = MutableStateFlow<List<Perfume>>(emptyList())
    val catalogo: StateFlow<List<Perfume>> = _catalogo

    init {
        // Carga inicial
        _catalogo.value = PerfumeRepository.getCatalogo()
    }
}
