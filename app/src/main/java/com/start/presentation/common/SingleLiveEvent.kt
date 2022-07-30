package com.start.presentation.common

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean

class EventLiveData<T> : SingleLiveEvent<Unit>()

open class SingleLiveEvent<T> : MutableLiveData<T>() {

    private val pending = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasActiveObservers()) {
            Timber.w("Multiple observers registeredObserver but only one will be notified of changes.")
        }

        // Observe the internal MutableLiveData
        super.observe(owner) {
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(it)
            }
        }
    }

    override fun observeForever(observer: Observer<in T>) {
        if (hasActiveObservers()) {
            Timber.w("Multiple observers registeredObserver but only one will be notified of changes.")
        }

        // Observe the internal MutableLiveData
        super.observeForever {
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(it)
            }
        }
    }

    @MainThread
    override fun setValue(t: T?) {
        pending.set(true)
        super.setValue(t)
    }
}

/**
 * Used for cases where T is Unit, to make calls cleaner.
 */
@MainThread
fun SingleLiveEvent<Unit>.call() {
    value = Unit
}
