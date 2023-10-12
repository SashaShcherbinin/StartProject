package base.arch.viewmodel.event

class OneTimeEvent<out T>(
    private val content: T,
) : Event<T> {

    private val consumedScopes by lazy { HashSet<Any>() }

    override fun isConsumed(scope: Any): Boolean {
        return scope in consumedScopes
    }

    override fun consume(scope: Any, block: (T) -> Unit) {
        if (!isConsumed(scope)) {
            consumedScopes.add(scope)
            block(content)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OneTimeEvent<*>

        if (content != other.content) return false
        if (consumedScopes != other.consumedScopes) return false

        return true
    }

    override fun hashCode(): Int {
        return content?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "OneTimeEvent(" +
                "content=$content, " +
                "consumedScopes=$consumedScopes)"
    }
}

fun <T : Any> T.asOneTimeEvent() = OneTimeEvent(content = this)
