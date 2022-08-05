package com.start.presentation.common

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

class TriggerStateFlow : EventStateFlow<Unit>() {

    fun call() {
        sendValue(Unit)
    }
}

open class EventStateFlow<T> : Flow<T> {

    private val channel: Channel<T> = Channel()

    private var _value: T? = null

    fun sendValue(value: T) {
        _value = value
        channel.trySend(value)
    }

    override suspend fun collect(collector: FlowCollector<T>) {
        if (_value != null) collector.emit(_value!!)
        channel.consumeAsFlow()
            .onEach {
                _value = null
            }
            .collect(collector)
    }
}
