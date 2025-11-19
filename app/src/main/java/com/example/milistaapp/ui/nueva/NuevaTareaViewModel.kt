package com.example.milistaapp.ui.nueva

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milistaapp.data.model.Tarea
import com.example.milistaapp.data.repository.TareasRepository
import kotlinx.coroutines.launch

class NuevaTareaViewModel : ViewModel() {

    private val repo = TareasRepository()

    fun guardarTarea(titulo: String, descripcion: String, onSuccess: () -> Unit) {

        val tarea = Tarea(
            titulo = titulo,
            descripcion = descripcion,
            completada = false
        )

        viewModelScope.launch {
            val ok = repo.agregarTarea(tarea)

            if (ok) {
                onSuccess() // Navega incluso sin internet
            }
        }
    }
}
