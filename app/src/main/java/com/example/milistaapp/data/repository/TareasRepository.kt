package com.example.milistaapp.data.repository

import com.example.milistaapp.data.model.Tarea
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class TareasRepository {

    private val db = FirebaseFirestore.getInstance()


    private val tareasRef = db.collection("tareas")

    suspend fun obtenerTareas(): List<Tarea> {
        return try {
            val snapshot = tareasRef.get().await()
            snapshot.documents.mapNotNull { doc ->
                doc.toObject(Tarea::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun agregarTarea(tarea: Tarea): Boolean {
        return try {

            // Generar ID local si aún no lo tiene
            val id = db.collection("tareas").document().id

            val tareaMap = mapOf(
                "titulo" to tarea.titulo,
                "descripcion" to tarea.descripcion,
                "completada" to tarea.completada
            )

            // ⚡ Guardar con set(): se guarda en cache aunque no haya internet
            tareasRef.document(id).set(tareaMap)

            true // devolvemos true SIEMPRE, offline/online
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun actualizarEstado(id: String, completada: Boolean) {
        try {
            tareasRef.document(id).update("completada", completada).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun eliminarTareasCompletadas(): Boolean {
        return try {
            // Obtener docs completadas
            val snapshot = tareasRef.whereEqualTo("completada", true).get().await()
            if (snapshot.isEmpty) return true // nada que borrar

            // Usar write batch para borrar en bloque (más eficiente)
            val batch = db.batch()
            for (doc in snapshot.documents) {
                batch.delete(doc.reference)
            }
            batch.commit().await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun eliminarTarea(id: String): Boolean {
        return try {
            tareasRef.document(id).delete().await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


    suspend fun actualizarTarea(id: String, nuevoTitulo: String, nuevaDescripcion: String) {
        try {
            tareasRef.document(id).update(
                mapOf(
                    "titulo" to nuevoTitulo,
                    "descripcion" to nuevaDescripcion
                )
            ).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}
