package e.palyvo.presentation.common.entity

import com.start.presentation.entity.ContentError

enum class ContentState {

    CONTENT,
    LOADING,
    ERROR,
    EMPTY;

    var contentError: ContentError? = null

    val isContent: Boolean get() = this == CONTENT

    val isLoading: Boolean get() = this == LOADING

    val isEmpty: Boolean get() = this == EMPTY

    val isError: Boolean get() = this == ERROR
}

val List<Any>.contentState: ContentState
    get() = when {
        isEmpty() -> ContentState.EMPTY
        else -> ContentState.CONTENT
    }
