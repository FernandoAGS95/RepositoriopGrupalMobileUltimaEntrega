package com.example.app_grupo7.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app_grupo7.model.Perfume
import com.example.app_grupo7.perfume.store.PerfumeStoreSqlite
import com.example.app_grupo7.ui.Dimens
import com.example.app_grupo7.util.vibrar
import com.example.app_grupo7.viewmodel.PerfumeViewModel
import kotlinx.coroutines.launch


private data class UiPerfume(
    val id: String,
    val nombre: String,
    val marca: String,
    val precio: Int,
    val ml: Int,
    val descripcion: String,
    val imageRes: Int? = null,
    val imageUri: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(
    vm: PerfumeViewModel = viewModel(),
    onGoPerfil: (() -> Unit)? = null,
    onGoCarrito: (() -> Unit)? = null,
    onAddToCart: ((id: String, nombre: String, precio: Int, imageRes: Int?,imageUri: String?) -> Unit)? = null
) {
    val repoPerfumes by vm.catalogo.collectAsState()

    val uiRepoPerfumes = remember(repoPerfumes) {
        repoPerfumes.map { p ->
            UiPerfume(
                id = p.id,
                nombre = p.nombre,
                marca = p.marca,
                precio = p.precio,
                ml = p.ml,
                descripcion = p.descripcion,
                imageRes = p.imageRes,
                imageUri = null
            )
        }
    }

    val context = LocalContext.current
    val store = remember { PerfumeStoreSqlite(context) }
    var uiDbPerfumes by remember { mutableStateOf<List<UiPerfume>>(emptyList()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        uiDbPerfumes = store.getAll().map { e ->
            UiPerfume(
                id = "db_${e.id ?: 0}",
                nombre = e.nombre ?: "Sin nombre",
                marca = "(Creado por ti)",
                precio = e.precio ?: 0,
                ml = 100,
                descripcion = "Agregado desde CRUD",
                imageRes = null,
                imageUri = e.imageUri
            )
        }
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val obs = LifecycleEventObserver { _, ev ->
            if (ev == Lifecycle.Event.ON_RESUME) {
                scope.launch {
                    uiDbPerfumes = store.getAll().map { e ->
                        UiPerfume(
                            id = "db_${e.id ?: 0}",
                            nombre = e.nombre ?: "Sin nombre",
                            marca = "(Creado por ti)",
                            precio = e.precio ?: 0,
                            ml = 100,
                            descripcion = "Agregado desde CRUD",
                            imageRes = null,
                            imageUri = e.imageUri
                        )
                    }
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(obs)
        onDispose { lifecycleOwner.lifecycle.removeObserver(obs) }
    }


    val perfumes = remember(uiRepoPerfumes, uiDbPerfumes) { uiRepoPerfumes + uiDbPerfumes }

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catálogo de perfumes") },
                actions = { onGoCarrito?.let { TextButton(onClick = it) { Text("Ver carrito") } } }
            )
        },
        snackbarHost = {
            AnimatedVisibility(
                visible = snackbarHostState.currentSnackbarData != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) { SnackbarHost(hostState = snackbarHostState) }
        }
    ) { inner ->
        val ctx = LocalContext.current
        val contentModifier = Modifier
            .padding(inner)
            .fillMaxSize()
            .padding(Dimens.screenPadding)

        AnimatedContent(
            targetState = perfumes.isEmpty(),
            modifier = contentModifier,
            label = "empty-vs-grid"
        ) { isEmpty ->
            if (isEmpty) {
                EmptyState(
                    icon = { Icon(Icons.Outlined.Inventory2, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                    title = "No hay perfumes disponibles",
                    subtitle = "Vuelve más tarde o recarga el catálogo."
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = Dimens.minGridCell),
                    verticalArrangement = Arrangement.spacedBy(Dimens.cardSpacing),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.cardSpacing)
                ) {
                    items(perfumes) { p ->
                        PerfumeCard(
                            p = p,
                            onAdd = {
                                vibrar(ctx)
                                onAddToCart?.invoke(p.id, p.nombre, p.precio, p.imageRes,p.imageUri)
                                scope.launch {
                                    snackbarHostState.showSnackbar("Agregado: ${p.nombre}")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyState(
    icon: @Composable () -> Unit,
    title: String,
    subtitle: String
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        icon()
        Spacer(Modifier.height(12.dp))
        Text(title, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(4.dp))
        Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun PerfumeCard(
    p: UiPerfume,
    onAdd: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {
        Column(Modifier.padding(Dimens.cardInner)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (p.imageRes != null) {
                    Image(
                        painter = painterResource(p.imageRes),
                        contentDescription = "${p.marca} ${p.nombre}",
                        modifier = Modifier
                            .size(84.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(Modifier.width(12.dp))
                } else {
                    p.imageUri?.let { uriStr ->
                        val context = LocalContext.current
                        val bitmap: Bitmap? = remember(uriStr) {
                            try {
                                val uri = Uri.parse(uriStr)
                                context.contentResolver.openInputStream(uri)?.use { input ->
                                    BitmapFactory.decodeStream(input)
                                }
                            } catch (_: Exception) { null }
                        }
                        if (bitmap != null) {
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "${p.marca} ${p.nombre}",
                                modifier = Modifier
                                    .size(84.dp)
                                    .clip(RoundedCornerShape(10.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(Modifier.width(12.dp))
                        }
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "${p.marca} — ${p.nombre}",
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "${p.ml} ml • $${p.precio}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            if (expanded) {
                Spacer(Modifier.height(8.dp))
                Text(p.descripcion, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(Modifier.height(12.dp))

            TextButton(
                onClick = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) { Text(if (expanded) "Ocultar" else "Ver más") }

            Spacer(Modifier.height(6.dp))

            Button(onClick = onAdd, modifier = Modifier.fillMaxWidth()) { Text("Agregar") }
        }
    }
}
