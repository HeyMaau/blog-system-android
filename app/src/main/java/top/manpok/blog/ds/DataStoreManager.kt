package top.manpok.blog.ds

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class DataStoreManager private constructor() {

    // At the top level of your kotlin file:
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data")

    companion object {
        private val KEY_HAS_INITIALIZED = booleanPreferencesKey("key_has_initialized")
        private val KEY_CURRENT_ENV = intPreferencesKey("key_current_env")
        private val KEY_LAST_CLOSE_UPDATE_DIALOG_TIME =
            intPreferencesKey("key_last_close_update_dialog_time")
        private val KEY_COUNTER_TITLE = stringPreferencesKey("key_counter_title")
        private val KEY_COUNTER_COUNT = intPreferencesKey("key_counter_count")
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

    fun getCurrentEnvSync(context: Context): Int {
        return runBlocking {
            context.dataStore.data.map {
                it[KEY_CURRENT_ENV] ?: 0
            }.first()
        }
    }

    suspend fun setCurrentEnv(context: Context, currentEnv: Int) {
        context.dataStore.edit {
            it[KEY_CURRENT_ENV] = currentEnv
        }
    }

    suspend fun setLastCloseUpdateDialogTime(context: Context, time: Int) {
        context.dataStore.edit {
            it[KEY_LAST_CLOSE_UPDATE_DIALOG_TIME] = time
        }
    }

    fun getLastCloseUpdateDialogTimeSync(context: Context): Int {
        return runBlocking {
            context.dataStore.data.map {
                it[KEY_LAST_CLOSE_UPDATE_DIALOG_TIME] ?: 0
            }.first()
        }
    }

    fun getCounterTitleSync(context: Context): String {
        return runBlocking {
            context.dataStore.data.map {
                it[KEY_COUNTER_TITLE] ?: "计数器"
            }.first()
        }
    }

    suspend fun setCounterTitle(context: Context, title: String) {
        context.dataStore.edit {
            it[KEY_COUNTER_TITLE] = title
        }
    }

    fun getCounterCountSync(context: Context): Int {
        return runBlocking {
            context.dataStore.data.map {
                it[KEY_COUNTER_COUNT] ?: 0
            }.first()
        }
    }

    suspend fun setCounterCount(context: Context, count: Int) {
        context.dataStore.edit {
            it[KEY_COUNTER_COUNT] = count
        }
    }

    fun setCounterCountSync(context: Context, count: Int) {
        runBlocking {
            context.dataStore.edit {
                it[KEY_COUNTER_COUNT] = count
            }
        }
    }
}