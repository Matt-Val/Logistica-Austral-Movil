package com.example.logistica_austral.model

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine

class Carrito private constructor(appContext: Context) { // Clase singleton (Jetpack)

    // Sharedpreferences, para guardar ids del carrito, funcionan como pequenios datos clave-valor (JetpackCompose), Futuro: DataStore
    private val prefs = appContext.getSharedPreferences("cart_prefs", Context.MODE_PRIVATE)
    private val KEY_IDS = "cart_ids"

    // carga los ids guardados
    private fun loadIds(): Set<Int> =
        // por su ids
        prefs.getStringSet(KEY_IDS, emptySet())!!
            .mapNotNull { it.toIntOrNull() }
            .toSet()

    private fun saveIds(ids: Set<Int>) {
        prefs.edit().putStringSet(KEY_IDS, ids.map { it.toString() }.toSet()).apply()
    }

    // stateflow para los ids del carrito
    private val _idsFlow = MutableStateFlow(loadIds())
    val idsFlow: StateFlow<Set<Int>> = _idsFlow

    suspend fun add(camionId: Int) {
        if (camionId in _idsFlow.value) return
        val updated = _idsFlow.value + camionId
        saveIds(updated)
        _idsFlow.value = updated // actualza el flow para que el observador detecte que el contenido ha cambiado
    }

    suspend fun remove(camionId: Int) {
        if (camionId !in _idsFlow.value) return
        val updated = _idsFlow.value - camionId
        saveIds(updated)
        _idsFlow.value = updated
    }

    // esta funcion devuelve un flow con los camiones que estan en el carrito
    fun observeCamiones(allCamionesFlow: StateFlow<List<Camion>>) =
        idsFlow.combine(allCamionesFlow) { ids, all -> all.filter { it.id in ids } }

    // patron singleton, permite que la clase tenga solo 1 instancia
    companion object {
        @Volatile private var INSTANCE: Carrito? = null
        fun getInstance(context: Context): Carrito =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Carrito(context.applicationContext).also { INSTANCE = it }
            }
    }
}