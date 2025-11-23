package com.example.app_grupo7.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.app_grupo7.viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumenScreen(vm: UsuarioViewModel) {
    val ui by vm.ui.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Resumen de registro") }) }
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Nombre: ${ui.nombre}")
            Text("Correo: ${ui.correo}")
            Text("Dirección: ${ui.direccion}")
            Text("Acepta términos: ${if (ui.aceptaTerminos) "Sí" else "No"}")
        }
    }
}


