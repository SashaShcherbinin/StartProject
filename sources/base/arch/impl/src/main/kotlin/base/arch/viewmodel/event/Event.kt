package base.arch.viewmodel.event

sealed interface Event<out T> {

    fun isConsumed(scope: Any = Unit): Boolean

    fun consume(scope: Any = Unit, block: (T) -> Unit)
}