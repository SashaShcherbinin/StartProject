@file:Suppress("OPT_IN_USAGE", "MemberVisibilityCanBePrivate")

package com.start.data.common.storage

import androidx.collection.LruCache
import com.start.data.common.cashe.CacheFactory
import com.start.data.common.cashe.CachePolicy
import com.start.data.common.cashe.CachedEntry
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import timber.log.Timber

@Suppress("unused", "EXPERIMENTAL_API_USAGE")
class LocalStorage<K : Any, E> constructor(
    maxElements: Int,
    private val cachePolicy: CachePolicy,
    private val network: (suspend (K) -> E),
    private val dataBase: DataBase<K, E>? = null
) {

    private val updateChannel = Channel<Unit>(1)
    private val cache: LruCache<K, CachedEntry<E>> = CacheFactory.createLruCache(maxElements)

    @Suppress("OPT_IN_USAGE")
    fun get(key: K): Flow<E> {
        val flow = flow {
            val cachedEntry: CachedEntry<E>? = cache[key]
            if (cachedEntry != null) {
                emit(cachedEntry.entry)
                if (cachePolicy.isExpired(cachedEntry)) {
                    ignoreNetworkException { fetchData(key) }
                }
            } else {
                val permanentEntry = dataBase?.read(key)
                if (permanentEntry != null) {
                    cache.put(key, CachePolicy.createEntry(permanentEntry))
                    emit(permanentEntry)
                    ignoreNetworkException { fetchData(key) }
                } else {
                    fetchData(key)
                }
            }
        }
        return merge(flowOf(Unit), updateChannel.receiveAsFlow())
            .flatMapLatest { flow }
    }

    private suspend fun ignoreNetworkException(function: suspend () -> Unit) {
        try {
            function()
        } catch (e: Throwable) {
            Timber.e(e)
        }
    }

    private suspend fun fetchData(key: K) {
        val value = network(key)
        cache.put(key, CachePolicy.createEntry(value))
        dataBase?.insertOrUpdate(key, value)
        updateChannel.trySend(Unit)
    }

    suspend fun update(
        key: K,
        onUpdateCallback: (E) -> E
    ) {
        val oldEntry = cache[key]?.entry
        if (oldEntry != null) {
            updateEntity(key, onUpdateCallback(oldEntry))
        } else {
            dataBase?.read(key)?.let { it ->
                updateEntity(key, onUpdateCallback(it))
            }
        }
    }

    private suspend fun updateEntity(key: K, newEntry: E) {
        dataBase?.insertOrUpdate(key, newEntry)
        cache.put(key, CachePolicy.createEntry(newEntry))
        updateChannel.trySend(Unit)
    }

    suspend fun refresh(key: K) {
        fetchData(key)
    }

    suspend fun clean(key: K) {
        cache.remove(key)
        dataBase?.remove(key)
        updateChannel.trySend(Unit)
    }

    suspend fun clean() {
        val snapshot = cache.snapshot()
        snapshot.keys.forEach {
            clean(it)
        }
    }

    interface DataBase<K, E> {
        suspend fun read(key: K): E?
        suspend fun remove(key: K)
        suspend fun insertOrUpdate(key: K, entity: E)
    }
}