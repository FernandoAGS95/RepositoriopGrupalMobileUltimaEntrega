package com.example.app_grupo7.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_grupo7.model.ExternalPost
import com.example.app_grupo7.repository.ExternalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExternalViewModel(
    private val repo: ExternalRepository = ExternalRepository()
) : ViewModel() {



    private val _posts = MutableStateFlow<List<ExternalPost>>(emptyList())
    val posts: StateFlow<List<ExternalPost>> = _posts

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadPosts() {
        viewModelScope.launch {
            _loading.value = true
            val result = repo.getPosts()
            _loading.value = false

            result.onSuccess { _posts.value = it }
            result.onFailure { _error.value = it.message }
        }
    }
}