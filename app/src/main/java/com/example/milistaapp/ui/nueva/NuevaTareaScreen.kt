package com.example.milistaapp.ui.nueva

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun NuevaTareaScreen(
    navController: NavController? = null,
    viewModel: NuevaTareaViewModel = viewModel()
) {

    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {

        Text("Nueva tarea", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.guardarTarea(titulo, descripcion) {
                    navController?.navigate("lista")   // vuelve al listado
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar")
        }
    }
}
