package top.manpok.blog.ds

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager private constructor() {

    // At the top level of your kotlin file:
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data")

    companion object {
        val KEY_HAS_INITIALIZED = booleanPreferencesKey("key_has_initialized")
        val KEY_CURRENT_ENV = intPreferencesKey("key_current_env")
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

    fun getCurrentEnv(context: Context): Flow<Int> {
        return context.dataStore.data.map {
            it[KEY_CURRENT_ENV] ?: 0
        }
    }

    suspend fun setCurrentEnv(context: Context, currentEnv: Int) {
        context.dataStore.edit {
            it[KEY_CURRENT_ENV] = currentEnv
        }
    }
}