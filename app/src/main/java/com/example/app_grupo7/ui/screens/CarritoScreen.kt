package com.example.app_grupo7.ui.screens

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.app_grupo7.cart.CarritoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    carritoVm: CarritoViewModel
) {
    val items by carritoVm.items.collectAsState()
    val total by carritoVm.total.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = { TopAppBar(title = { Text("Carrito") }) }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
        ) {
            if (items.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Carrito vacío",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(items) { it ->
                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                when {
                                    it.imageRes != null -> {
                                        Image(
                                            painter = painterResource(it.imageRes),
                                            contentDescription = it.nombre,
                                            modifier = Modifier
                                                .size(60.dp)
                                                .clip(RoundedCornerShape(10.dp))
                                        )
                                    }
                                    it.imageUri != null -> {
                                        val bmp = remember(it.imageUri) {
                                            try {
                                                val uri = Uri.parse(it.imageUri)
                                                context.contentResolver.openInputStream(uri)?.use { input ->
                                                    BitmapFactory.decodeStream(input)
                                                }
                                            } catch (_: Exception) { null }
                                        }
                                        if (bmp != null) {
                                            Image(
                                                bitmap = bmp.asImageBitmap(),
                                                contentDescription = it.nombre,
                                                modifier = Modifier
                                                    .size(60.dp)
                                                    .clip(RoundedCornerShape(10.dp))
                                            )
                                        }
                                    }
                                }

                                Spacer(Modifier.width(12.dp))

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = it.nombre,
                                        style = MaterialTheme.typography.titleMedium,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    Text(
                                        text = "$${it.precio} c/u",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(Modifier.height(10.dp))

                                    QuantityStepper(
                                        quantity = it.quantity,
                                        onDecrement = { carritoVm.setQty(it.perfumeId, it.quantity - 1) },
                                        onIncrement = { carritoVm.setQty(it.perfumeId, it.quantity + 1) },
                                        onRemove = { carritoVm.remove(it.perfumeId) }
                                    )
                                }

                                Spacer(Modifier.width(8.dp))
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "Subtotal",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "$${it.precio * it.quantity}",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }
                        }
                    }
                }

                Divider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Total", style = MaterialTheme.typography.titleLarge)
                        Text("$${total}", style = MaterialTheme.typography.titleLarge)
                    }
                    Spacer(Modifier.height(10.dp))
                    Button(
                        onClick = { carritoVm.clear() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Vaciar carrito")
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun QuantityStepper(
    quantity: Int,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit,
    onRemove: () -> Unit
) {
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val compact = maxWidth < 360.dp

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (compact) {
                FilledTonalIconButton(onClick = onDecrement, modifier = Modifier.size(36.dp)) {
                    Icon(Icons.Outlined.Remove, contentDescription = "Menos")
                }
                Spacer(Modifier.width(8.dp))
                Text(text = "x$quantity", style = MaterialTheme.typography.titleSmall)
                Spacer(Modifier.width(8.dp))
                FilledTonalIconButton(onClick = onIncrement, modifier = Modifier.size(36.dp)) {
                    Icon(Icons.Outlined.Add, contentDescription = "Más")
                }
            } else {
                OutlinedButton(
                    onClick = onDecrement,
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.height(40.dp)
                ) { Text("-") }

                Spacer(Modifier.width(10.dp))

                Text(text = "x$quantity", style = MaterialTheme.typography.titleSmall)

                Spacer(Modifier.width(10.dp))

                OutlinedButton(
                    onClick = onIncrement,
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.height(40.dp)
                ) { Text("+") }
            }

            Spacer(Modifier.weight(1f))

            if (compact) {
                IconButton(onClick = onRemove) {
                    Icon(Icons.Outlined.Delete, contentDescription = "Quitar")
                }
            } else {
                TextButton(
                    onClick = onRemove,
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.height(40.dp)
                ) {
                    Icon(Icons.Outlined.Delete, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("Quitar")
                }
            }
        }
    }
}
