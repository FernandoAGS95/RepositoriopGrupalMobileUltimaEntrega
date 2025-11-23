package com.example.app_grupo7.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.app_grupo7.data.AppState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotasScreen(navController: NavController, appState: AppState){
    var nota by remember { mutableStateOf("") }

    Scaffold (
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor =  MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text("Notas") },
                actions = {
                    TextButton(onClick = {
                        appState.logout()
                        navController.navigate("login") {
                            popUpTo("login") { inclusive = true }
                        }
                    }) {
                        Text("Salir",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold)
                    }
                })
        }
    ){ padding ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ){
            OutlinedTextField(
                value = nota,
                onValueChange = { nota = it },
                label = { Text("Escribe una nota")},
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    if (nota.isNotBlank()){
                        appState.agregarNotas(nota)
                        nota = ""
                    }
                }
            ) { Text("Guardar Nota") }

            Spacer(Modifier.height(16.dp))
            Text("Notas Guardadas:")

            LazyColumn {
                itemsIndexed(appState.obtenerNotas()) { index, n ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("• $n")
                        TextButton(onClick = { appState.borrarNotas(index) }) {
                            Text("Borrar", color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}
