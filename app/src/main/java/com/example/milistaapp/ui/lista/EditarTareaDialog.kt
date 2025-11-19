package com.example.milistaapp.ui.lista

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.milistaapp.data.model.Tarea

@Composable
fun EditarTareaDialog(
    tarea: Tarea,
    onDismiss: () -> Unit,
    onGuardar: (String, String) -> Unit
) {
    var titulo by remember { mutableStateOf(tarea.titulo) }
    var descripcion by remember { mutableStateOf(tarea.descripcion) }

    AlertDialog(
        onDismissRequest = { onDismiss() },

        confirmButton = {
            Button(onClick = {
                onGuardar(titulo, descripcion)
            }) {
                Text("Guardar")
            }
        },

        dismissButton = {
            OutlinedButton(onClick = { onDismiss() }) {
                Text("Cancelar")
            }
        },

        title = { Text("Editar tarea") },

        text = {
            Column {

                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}