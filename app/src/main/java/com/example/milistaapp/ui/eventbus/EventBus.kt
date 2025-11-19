package com.example.milistaapp.ui.eventbus

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

object EventBus {

    private val _tareasActualizadas = MutableSharedFlow<Unit>()
    val tareasActualizadas: SharedFlow<Unit> = _tareasActualizadas

    suspend fun enviarActualizacion() {
        _tareasActualizadas.emit(Unit)
    }
}
