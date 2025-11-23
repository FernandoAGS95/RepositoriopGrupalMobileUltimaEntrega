package com.example.app_grupo7.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.app_grupo7.viewmodel.ProductTCGViewModel
import com.example.app_grupo7.model.ProductTCG

@Composable
fun ProductTCGDetailScreen(
    productId: Long?,
    viewModel: ProductTCGViewModel = viewModel()
) {
    if (productId == null) {
        ErrorState("ID inválido")
        return
    }

    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val products by viewModel.products.collectAsState()

    // Intentar obtenerlo localmente si ya fue cargado en la lista
    val localProduct = products.firstOrNull { it.id == productId }

    // Si no está en la lista, entonces pedirlo al backend
    LaunchedEffect(productId) {
        if (localProduct == null) {
            viewModel.getProductById(productId)
        }
    }

    val product = localProduct ?: products.firstOrNull()

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            loading -> LoadingState()
            error != null -> ErrorState(error!!)
            product != null -> ProductTCGDetailContent(product)
            else -> Text(
                text = "Producto no encontrado",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun ProductTCGDetailContent(product: ProductTCG) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Imagen principal
        if (product.imagen.isNotBlank()) {
            Image(
                painter = rememberAsyncImagePainter(product.imagenUrlCompleta),
                contentDescription = product.nombre,
                modifier = Modifier
                    .size(250.dp)
                    .padding(bottom = 16.dp)
            )
        }

        Text(
            text = product.nombre,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = product.descripcion,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "Precio: $${product.precio}",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Si tiene oferta
        if (!product.oferta.isNullOrBlank()) {
            Text(
                text = "Oferta: ${product.oferta}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Imagen hover opcional
        if (!product.hover.isNullOrBlank()) {
            Text(
                text = "Vista alternativa:",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(8.dp))

            Image(
                painter = rememberAsyncImagePainter(product.hoverUrlCompleta),
                contentDescription = "Imagen Hover",
                modifier = Modifier.size(200.dp)
            )
        }
    }
}
