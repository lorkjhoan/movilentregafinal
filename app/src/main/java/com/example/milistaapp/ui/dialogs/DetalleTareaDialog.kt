package com.example.milistaapp.ui.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.milistaapp.data.model.Tarea

@Composable
fun DetalleTareaDialog(
    tarea: Tarea,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = tarea.titulo)
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = tarea.descripcion ?: "Sin descripción",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (tarea.completada) "✔ Completada" else "⏳ Pendiente",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}