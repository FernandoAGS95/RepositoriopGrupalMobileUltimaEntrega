package com.example.app_grupo7.screen


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app_grupo7.viewmodel.ExternalViewModel

@Composable
fun ExternalApiScreen(vm: ExternalViewModel = viewModel()) {
    val loading by vm.loading.collectAsState()
    val posts by vm.posts.collectAsState()
    val error by vm.error.collectAsState()

    LaunchedEffect(Unit) {
        vm.loadPosts()
    }

    when {
        loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }

        error != null -> Text("Error: $error")

        else -> LazyColumn(Modifier.padding(16.dp)) {
            items(posts) { post ->
                Card(
                    Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text(post.title, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(6.dp))
                        Text(post.body)
                    }
                }
            }
        }
    }
}