package com.start.data.common.cashe

import androidx.annotation.NonNull
import java.util.concurrent.TimeUnit

class CachePolicy private constructor(private val time: Int, private val timeUnit: TimeUnit) {

    fun isExpired(entry: CachedEntry<*>): Boolean {
        val currentTime = getTime()
        return currentTime - entry.getCreateTime() > timeUnit.toMillis(time.toLong())
    }

    @Suppress("unused")
    companion object {

        fun create(time: Int, timeUnit: TimeUnit): CachePolicy {
            return CachePolicy(time, timeUnit)
        }

        fun infinite(): CachePolicy {
            return CachePolicy(Integer.MAX_VALUE, TimeUnit.DAYS)
        }

        fun <T> createEntry(@NonNull t: T): CachedEntry<T> {
            return CachedEntry(t, getTime())
        }

        private fun getTime(): Long {
            return System.currentTimeMillis()
        }
    }
}