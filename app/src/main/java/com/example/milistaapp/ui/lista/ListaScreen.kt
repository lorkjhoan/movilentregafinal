package com.example.milistaapp.ui.lista

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Box
import com.example.milistaapp.data.model.Tarea
import com.example.milistaapp.ui.dialogs.DetalleTareaDialog

@OptIn(androidx.compose.material.ExperimentalMaterialApi::class)
@Composable
fun ListaScreen(viewModel: ListaViewModel = viewModel()) {

    val tareas by viewModel.tareasFiltradas.collectAsState()
    val filtro by viewModel.filtro.collectAsState()

    // 🟦 Estados de diálogos
    var tareaAEditar by remember { mutableStateOf<Tarea?>(null) }
    var tareaADetalle by remember { mutableStateOf<Tarea?>(null) }

    LaunchedEffect(Unit) {
        viewModel.cargarTareas()
    }

    Column(modifier = Modifier.padding(16.dp)) {

        Text(
            "Mis tareas",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FiltroButton(
                texto = "Todas",
                seleccionado = filtro == FiltroTareas.TODAS,
                onClick = { viewModel.cambiarFiltro(FiltroTareas.TODAS) }
            )

            FiltroButton(
                texto = "Pendientes",
                seleccionado = filtro == FiltroTareas.PENDIENTES,
                onClick = { viewModel.cambiarFiltro(FiltroTareas.PENDIENTES) }
            )

            FiltroButton(
                texto = "Completadas",
                seleccionado = filtro == FiltroTareas.COMPLETADAS,
                onClick = { viewModel.cambiarFiltro(FiltroTareas.COMPLETADAS) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (tareas.isEmpty()) {
            Text("No hay tareas en este filtro.")
        } else {
            LazyColumn {
                items(
                    items = tareas,
                    key = { it.id }
                ) { tarea ->

                    val dismissState = rememberDismissState(
                        confirmStateChange = { value ->
                            if (value == DismissValue.DismissedToStart) {
                                viewModel.eliminarTarea(tarea)
                                true
                            } else false
                        }
                    )

                    Box(modifier = Modifier.fillMaxWidth()) {

                        if (dismissState.dismissDirection != null) {
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .padding(vertical = 8.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.error),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Eliminar",
                                    tint = MaterialTheme.colorScheme.onError,
                                    modifier = Modifier.padding(end = 20.dp)
                                )
                            }
                        }

                        SwipeToDismiss(
                            state = dismissState,
                            directions = setOf(DismissDirection.EndToStart),
                            background = {},
                            dismissContent = {
                                TareaItem(
                                    tarea = tarea,
                                    viewModel = viewModel,
                                    onEditar = { tareaAEditar = it },
                                    onVerDetalle = { tareaADetalle = it } // 🟦 Abrir diálogo
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    // 🟦 Diálogo de editar
    tareaAEditar?.let { tarea ->
        EditarTareaDialog(
            tarea = tarea,
            onDismiss = { tareaAEditar = null },
            onGuardar = { nuevoTitulo, nuevaDescripcion ->
                viewModel.editarTarea(
                    tarea.copy(
                        titulo = nuevoTitulo,
                        descripcion = nuevaDescripcion
                    )
                )
                tareaAEditar = null
            }
        )
    }

    // 🟦 Diálogo de detalles
    tareaADetalle?.let { tarea ->
        DetalleTareaDialog(
            tarea = tarea,
            onDismiss = { tareaADetalle = null }
        )
    }
}

@Composable
fun FiltroButton(texto: String, seleccionado: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (seleccionado) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Text(texto)
    }
}

@Composable
fun TareaItem(
    tarea: Tarea,
    viewModel: ListaViewModel,
    onEditar: (Tarea) -> Unit,
    onVerDetalle: (Tarea) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onVerDetalle(tarea) },  // 🟦 Mostrar detalles
        shape = RoundedCornerShape(12.dp)
    ) {

        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(modifier = Modifier.weight(1f)) {
                Text(tarea.titulo, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(tarea.descripcion, style = MaterialTheme.typography.bodyMedium)
            }

            // 🟦 Icono de editar
            IconButton(onClick = { onEditar(tarea) }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar tarea"
                )
            }

            Checkbox(
                checked = tarea.completada,
                onCheckedChange = { nuevoEstado ->
                    viewModel.cambiarEstado(tarea, nuevoEstado)
                }
            )
        }
    }
}
