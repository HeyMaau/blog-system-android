package top.manpok.blog.ds

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager private constructor() {

    // At the top level of your kotlin file:
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data")

    companion object {
        val KEY_HAS_INITIALIZED = booleanPreferencesKey("key_has_initialized")
        val instance: DataStoreManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            DataStoreManager()
        }
    }

    fun getHasInitialized(context: Context): Flow<Boolean> {
        return context.dataStore.data.map {
            it[KEY_HAS_INITIALIZED] ?: false
        }
    }

    suspend fun setHasInitialized(context: Context, hasInitialized: Boolean) {
        context.dataStore.edit {
            it[KEY_HAS_INITIALIZED] = hasInitialized
        }
    }
}