package com.example.milistaapp.ui.configuracion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milistaapp.data.repository.TareasRepository
import com.example.milistaapp.ui.eventbus.EventBus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ConfigViewModel(
    private val repo: TareasRepository = TareasRepository()
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    fun eliminarCompletadas() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val ok = repo.eliminarTareasCompletadas()
                if (ok) {

                    // 🔥 Notificar a la app que hubo cambios en las tareas
                    EventBus.enviarActualizacion()

                    _message.value = "Tareas completadas eliminadas correctamente."
                } else {
                    _message.value = "No se pudieron eliminar las tareas completadas."
                }
            } catch (e: Exception) {
                _message.value = "Error: ${e.message ?: "desconocido"}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}