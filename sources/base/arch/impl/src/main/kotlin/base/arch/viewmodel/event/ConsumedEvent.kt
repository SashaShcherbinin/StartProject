package base.arch.viewmodel.event

data object ConsumedEvent : Event<Nothing> {

    override fun isConsumed(scope: Any): Boolean = true

    override fun consume(scope: Any, block: (Nothing) -> Unit) = Unit

}
