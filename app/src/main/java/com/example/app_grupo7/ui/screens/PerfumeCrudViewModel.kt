package com.example.app_grupo7.perfume.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.app_grupo7.perfume.store.PerfumeEntity
import com.example.app_grupo7.perfume.store.PerfumeStoreSqlite
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PerfumeCrudViewModel(
    private val store: PerfumeStoreSqlite
) : ViewModel() {

    private val _perfumes = MutableStateFlow<List<PerfumeEntity>>(emptyList())
    val perfumes: StateFlow<List<PerfumeEntity>> = _perfumes

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _perfumes.value = store.getAll()
        }
    }

    fun create(nombre: String, precio: Int, uri: String?) {
        viewModelScope.launch {
            store.create(nombre, precio, uri)
            refresh()
        }
    }

    fun update(id: Long, nombre: String, precio: Int, uri: String?) {
        viewModelScope.launch {
            store.update(id, nombre, precio, uri)
            refresh()
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch {
            store.delete(id)
            refresh()
        }
    }
}

class PerfumeCrudVMFactory(
    private val store: PerfumeStoreSqlite
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return PerfumeCrudViewModel(store) as T
    }
}
