package com.example.milistaapp.ui.configuracion

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment

@Composable
fun ConfigScreen(viewModel: ConfigViewModel = viewModel()) {

    val loading by viewModel.loading.collectAsState()
    val message by viewModel.message.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // estado del dialogo de confirmación
    var dialogShown by remember { mutableStateOf(false) }

    // Mostrar snackbar cuando viewModel.message cambie
    LaunchedEffect(message) {
        message?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                "Configuración",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Ejemplo simple de switch
            var ejemplo by remember { mutableStateOf(false) }
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Text("Opción de ejemplo")
                Spacer(modifier = Modifier.weight(1f))
                Switch(checked = ejemplo, onCheckedChange = { ejemplo = it })
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Botón que abre el diálogo
            Button(
                onClick = { dialogShown = true },
                enabled = !loading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text("Eliminar tareas completadas")
            }
        }

        // Snackbar host en el fondo
        SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter))
    }

    // Diálogo de confirmación
    if (dialogShown) {
        AlertDialog(
            onDismissRequest = { dialogShown = false },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Estás seguro que deseas eliminar todas las tareas marcadas como completadas? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = {
                    dialogShown = false
                    // Llamamos al ViewModel que hará el trabajo en background
                    viewModel.eliminarCompletadas()
                }) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { dialogShown = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
