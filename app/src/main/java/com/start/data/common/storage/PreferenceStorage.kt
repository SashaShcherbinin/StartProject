@file:Suppress("EXPERIMENTAL_API_USAGE", "unused", "OPT_IN_USAGE", "MemberVisibilityCanBePrivate")

package com.start.data.common.storage

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import timber.log.Timber

class PreferenceStorage(context: Context, name: String) {

    @Suppress("EXPERIMENTAL_API_USAGE")
    private val updateChannel = Channel<Unit>(1)
    private var preferences: SharedPreferences = context
        .getSharedPreferences(name, Context.MODE_PRIVATE)

    suspend fun update(save: (SharedPreferences.Editor) -> Unit) {
        val edit = preferences.edit()
        savePreferences(save, edit)
        updateChannel.trySend(Unit)
    }

    private suspend fun savePreferences(
        save: (SharedPreferences.Editor) -> Unit,
        edit: SharedPreferences.Editor
    ) {
        save.invoke(edit)
        withContext(Dispatchers.IO) {
            edit.commit()
        }
    }

    @Suppress("EXPERIMENTAL_API_USAGE")
    fun <T> observe(actionGet: (SharedPreferences) -> T): Flow<T> =
        merge(flowOf(Unit), updateChannel.receiveAsFlow())
            .map {
                actionGet.invoke(preferences)
            }
            .catch { e ->
                Timber.e(e)
                clear()
                emit(actionGet.invoke(preferences))
            }

    suspend fun clear() {
        withContext(Dispatchers.IO) {
            preferences.edit().clear().commit()
        }
    }

    fun clearNow() {
        preferences.edit().clear().apply()
    }

    companion object {
        @JvmStatic
        fun clear(context: Context, names: Array<String>) {
            for (name in names) {
                context.getSharedPreferences(name, Context.MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply()
            }
        }
    }
}
