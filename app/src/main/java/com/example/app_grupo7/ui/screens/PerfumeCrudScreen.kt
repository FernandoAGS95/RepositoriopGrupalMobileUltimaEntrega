package com.example.app_grupo7.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app_grupo7.perfume.store.PerfumeEntity
import com.example.app_grupo7.perfume.store.PerfumeStoreSqlite
import com.example.app_grupo7.perfume.vm.PerfumeCrudVMFactory
import com.example.app_grupo7.perfume.vm.PerfumeCrudViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfumeCrudScreen() {
    val context = LocalContext.current
    val vm: PerfumeCrudViewModel =
        viewModel(factory = PerfumeCrudVMFactory(PerfumeStoreSqlite(context)))
    val items by vm.perfumes.collectAsState()

    var showEditor by remember { mutableStateOf(false) }
    var editing by remember { mutableStateOf<PerfumeEntity?>(null) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("CRUD Perfumes (SQLite)") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                editing = null
                showEditor = true
            }) { Icon(Icons.Outlined.Add, contentDescription = "Agregar") }
        }
    ) { inner ->
        Box(Modifier.padding(inner).fillMaxSize()) {
            if (items.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Sin perfumes. Agrega uno con +")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(items) { p ->
                        PerfumeRow(
                            p = p,
                            onEdit = { editing = p; showEditor = true },
                            onDelete = { p.id?.let(vm::delete) }
                        )
                    }
                }
            }
        }
    }

    if (showEditor) {
        PerfumeEditorDialog(
            initial = editing,
            onDismiss = { showEditor = false },
            onConfirm = { nombre, precio, uri ->
                if (editing == null) vm.create(nombre, precio, uri?.toString())
                else vm.update(editing!!.id!!, nombre, precio, uri?.toString())
                showEditor = false
            }
        )
    }
}

@Composable
private fun PerfumeRow(
    p: PerfumeEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    ElevatedCard(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val context = LocalContext.current
            val bitmap by remember(p.imageUri) {
                mutableStateOf(p.imageUri?.let { loadBitmapFromUri(context, Uri.parse(it)) })
            }
            Image(
                bitmap = (bitmap ?: placeholderBitmap()).asImageBitmap(),
                contentDescription = p.nombre ?: "perfume",
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(p.nombre ?: "Sin nombre", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(4.dp))
                Text(
                    "$${p.precio ?: 0}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onEdit) { Icon(Icons.Outlined.Edit, contentDescription = "Editar") }
            IconButton(onClick = onDelete) { Icon(Icons.Outlined.Delete, contentDescription = "Borrar") }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PerfumeEditorDialog(
    initial: PerfumeEntity?,
    onDismiss: () -> Unit,
    onConfirm: (nombre: String, precio: Int, imageUri: Uri?) -> Unit
) {
    val context = LocalContext.current

    var nombre by remember { mutableStateOf(TextFieldValue(initial?.nombre ?: "")) }
    var precioText by remember { mutableStateOf(TextFieldValue(initial?.precio?.toString() ?: "")) }
    var imageUri by remember { mutableStateOf(initial?.imageUri?.let(Uri::parse)) }

    var tempCaptureUri by remember { mutableStateOf<Uri?>(null) }
    val takePicture = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imageUri = tempCaptureUri
        }
    }

    val previewBitmap by remember(imageUri) {
        mutableStateOf(imageUri?.let { loadBitmapFromUri(context, it) })
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initial == null) "Agregar colonia" else "Editar colonia") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = precioText,
                    onValueChange = { precioText = it },
                    label = { Text("Precio") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button(onClick = {
                        tempCaptureUri = createImageUri(context)
                        tempCaptureUri?.let { takePicture.launch(it) }
                    }) {
                        Text(if (imageUri == null) "Tomar foto" else "Re-tomar foto")
                    }
                    Spacer(Modifier.width(12.dp))
                    if (previewBitmap != null) {
                        Image(
                            bitmap = previewBitmap!!.asImageBitmap(),
                            contentDescription = "preview",
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val precio = precioText.text.toIntOrNull() ?: 0
                onConfirm(nombre.text.trim(), precio, imageUri)
            }) { Text(if (initial == null) "Guardar" else "Actualizar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}


private fun loadBitmapFromUri(context: Context, uri: Uri): Bitmap? {
    return try {
        if (Build.VERSION.SDK_INT >= 28) {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                decoder.isMutableRequired = true
            }
        } else {
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        }
    } catch (_: Exception) {
        null
    }
}

private fun placeholderBitmap(): Bitmap {
    return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888).apply {
        eraseColor(android.graphics.Color.LTGRAY)
    }
}


private fun createImageUri(context: Context): Uri? {
    return try {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis())
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile("IMG_$timeStamp", ".jpg", storageDir)
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            image
        )
    } catch (_: Exception) {
        null
    }
}
