package com.cheiviz.habit_tracker


import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        val HABITOS_KEY = stringSetPreferencesKey("habitos")
        val NOTIFICACIONES_KEY = booleanPreferencesKey("notificaciones")
        /*val TEMA_KEY = stringPreferencesKey("tema")*/
    }

    // Guardar hábito
    suspend fun agregarHabito(habito: String) {
        context.dataStore.edit { prefs ->
            val lista = prefs[HABITOS_KEY]?.toMutableSet() ?: mutableSetOf()
            lista.add(habito)
            prefs[HABITOS_KEY] = lista
        }
    }

    // Obtener lista de hábitos
    val habitos: Flow<List<String>> = context.dataStore.data
        .map { prefs -> prefs[HABITOS_KEY]?.toList() ?: emptyList() }

    // Guardar notificaciones
    suspend fun setNotificaciones(activado: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[NOTIFICACIONES_KEY] = activado
        }
    }

    // Leer notificaciones
    val notificaciones: Flow<Boolean> = context.dataStore.data
        .map { prefs -> prefs[NOTIFICACIONES_KEY] ?: false }

    /*// Guardar tema
    suspend fun setTema(tema: String) {
        context.dataStore.edit { prefs ->
            prefs[TEMA_KEY] = tema
        }
    }

    // Leer tema
    val tema: Flow<String> = context.dataStore.data
        .map { prefs -> prefs[TEMA_KEY] ?: "claro" }*/
}
