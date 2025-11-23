package com.example.app_grupo7.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import coil.compose.rememberAsyncImagePainter
import com.example.app_grupo7.model.ProductTCG
import com.example.app_grupo7.viewmodel.ProductTCGViewModel

@Composable
fun ProductTCGScreen(
    viewModel: ProductTCGViewModel = viewModel(),
    onProductClick: (ProductTCG) -> Unit = {}
) {
    val loading by viewModel.loading.collectAsState()
    val products by viewModel.products.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadProducts()
    }

    Box(modifier = Modifier.fillMaxSize()) {

        when {
            loading -> LoadingState()
            error != null -> ErrorState(error!!)
            else -> ProductTCGList(
                products = products,
                onProductClick = onProductClick
            )
        }
    }
}

@Composable
fun ProductTCGList(
    products: List<ProductTCG>,
    onProductClick: (ProductTCG) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(products) { product ->
            ProductCard(
                product = product,
                onClick = { onProductClick(product) }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun ProductCard(product: ProductTCG, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp)) {

            Image(
                painter = rememberAsyncImagePainter(product.imagenUrlCompleta),
                contentDescription = product.nombre,
                modifier = Modifier.size(90.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.nombre,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = product.descripcion,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "$${product.precio}",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Composable
fun LoadingState() {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorState(msg: String) {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Error: $msg")
    }
}
