package com.example.materialplayer.data.preferences

import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import androidx.core.net.toUri

class RootsPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val ROOTS_KEY = stringSetPreferencesKey("roots")
    }

    /** Поток сохранённых URI корневых папок */
    val rootsFlow: Flow<List<Uri>> = dataStore.data
        .map { prefs ->
            prefs[ROOTS_KEY]
                ?.mapNotNull { raw ->
                    runCatching { raw.toUri() }.getOrNull()
                } ?: emptyList()
        }

    /** Добавить новый корень */
    suspend fun addRoot(uri: Uri) {
        dataStore.edit { prefs ->
            val current = prefs[ROOTS_KEY] ?: emptySet()
            prefs[ROOTS_KEY] = current + uri.toString()
        }
    }

    /** Удалить существующий корень */
    suspend fun removeRoot(uri: Uri) {
        dataStore.edit { prefs ->
            val current = prefs[ROOTS_KEY] ?: emptySet()
            prefs[ROOTS_KEY] = current - uri.toString()
        }
    }
}
