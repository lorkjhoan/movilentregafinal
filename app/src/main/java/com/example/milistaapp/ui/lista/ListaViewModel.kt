package com.example.milistaapp.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milistaapp.data.model.Tarea
import com.example.milistaapp.data.repository.TareasRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class FiltroTareas { TODAS, PENDIENTES, COMPLETADAS }

class ListaViewModel : ViewModel() {

    private val repo = TareasRepository()

    private val _tareas = MutableStateFlow<List<Tarea>>(emptyList())
    val tareas: StateFlow<List<Tarea>> = _tareas

    private val _filtro = MutableStateFlow(FiltroTareas.TODAS)
    val filtro: StateFlow<FiltroTareas> = _filtro

    // LISTA FILTRADA QUE USARÁ LA UI
    val tareasFiltradas: StateFlow<List<Tarea>> =
        combine(_tareas, _filtro) { lista, filtro ->
            when (filtro) {
                FiltroTareas.TODAS -> lista
                FiltroTareas.PENDIENTES -> lista.filter { !it.completada }
                FiltroTareas.COMPLETADAS -> lista.filter { it.completada }
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    init {
        cargarTareas()
    }

    fun cargarTareas() {
        viewModelScope.launch {
            _tareas.value = repo.obtenerTareas()
        }
    }

    fun cambiarEstado(tarea: Tarea, nuevoEstado: Boolean) {
        viewModelScope.launch {
            repo.actualizarEstado(tarea.id, nuevoEstado)
            cargarTareas()
        }
    }

    fun cambiarFiltro(nuevoFiltro: FiltroTareas) {
        _filtro.value = nuevoFiltro
    }

    fun eliminarTarea(tarea: Tarea) {
        viewModelScope.launch {
            repo.eliminarTarea(tarea.id)
            cargarTareas()
        }
    }

    fun editarTarea(tarea: Tarea) {
        viewModelScope.launch {
            repo.actualizarTarea(
                id = tarea.id,
                nuevoTitulo = tarea.titulo,
                nuevaDescripcion = tarea.descripcion
            )
            cargarTareas()
        }
    }

}
