package com.start.data.common.cashe

import androidx.collection.LruCache
import java.lang.ref.WeakReference
import java.util.*

object CacheFactory {

    private val list = LinkedList<WeakReference<LruCache<*, *>>>()

    fun <K, E> createLruCache(maxSize: Int): LruCache<K, E> = LruCache<K, E>(maxSize).apply {
        list.removeAll {
            it.get() == null
        }
        list.add(WeakReference(this))
    }

    fun clean() {
        list.forEach {
            it.get()?.evictAll()
        }
    }
}