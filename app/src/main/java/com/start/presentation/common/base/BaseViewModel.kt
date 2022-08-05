@file:Suppress("unused", "OPT_IN_USAGE", "MemberVisibilityCanBePrivate")

package com.start.presentation.common.base

import android.content.res.Resources
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.start.presentation.common.ErrorHandler
import com.start.presentation.common.EventStateFlow
import com.start.presentation.entity.ContentError
import e.palyvo.presentation.common.entity.ContentState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@Suppress("TooManyFunctions")
abstract class BaseViewModel : ViewModel() {

    val theme = MutableStateFlow<Resources.Theme?>(null)
    val showErrorEvent = EventStateFlow<ContentError>()
    val messageEvent = EventStateFlow<String>()
    val uploadingState = MutableStateFlow(false)
    val contentState = MutableStateFlow(ContentState.LOADING)

    private val _retryEvent = Channel<Unit>(1).apply { trySend(Unit) }

    val errorHandler: ErrorHandler = ErrorHandler()

    lateinit var savedStateHandle: SavedStateHandle

    /**
     * Method to execute one time coroutines, for example, save something or update (suspend method)
     * @param showUploading show blocking uploading dialog or not
     * @param customErrorHandler custom handing of ContentError to show in another place
     * @param onStart action what called before start
     * @param onFinal action what called when success or error
     * @param run suspend function what will be executed in this scope
     * */
    @Suppress("LongParameterList", "TooGenericExceptionCaught")
    protected fun CoroutineScope.execute(
        showUploading: Boolean = true,
        customErrorHandler: ((e: Throwable) -> Boolean)? = null,
        onStart: (() -> Unit)? = null,
        onFinal: (() -> Unit)? = null,
        run: suspend () -> Unit,
    ) {
        launch {
            onStart?.invoke()
            if (showUploading) uploadingState.value = true
            try {
                run()
            } catch (throwable: Throwable) {
                errorHandler.handleRequestError(throwable) { viewError ->
                    val handled: Boolean = customErrorHandler?.invoke(throwable) ?: false
                    viewError
                        .takeIf { handled.not() }
                        ?.let {
                            showErrorEvent.sendValue(it)
                        }
                }
            } finally {
                if (showUploading) uploadingState.value = false
                onFinal?.invoke()
            }
        }
    }


    /**
     * Connection logic with content state with error handling
     * @param emptyHandler logic to show empty state
     * @param customError to handle error not in the default way
     * */
    protected fun <T> Flow<T>.connectContentState(
        emptyHandler: ((T) -> Boolean)? = null,
        customError: ((e: Throwable) -> Boolean)? = null
    ): Flow<T> {
        return _retryEvent.receiveAsFlow().flatMapLatest {
            this@connectContentState
                .catchError(customError)
                .onStart {
                    contentState.value = ContentState.LOADING
                }
        }.onEach { data ->
            contentState.value = emptyHandler?.let {
                if (it.invoke(data)) ContentState.EMPTY else ContentState.CONTENT
            } ?: ContentState.CONTENT
        }
    }

    fun retry() {
        _retryEvent.trySend(Unit)
    }

    protected fun <T> Flow<T>.catchError(
        customError: ((e: Throwable) -> Boolean)? = null,
    ): Flow<T> = catch { e ->
        if (customError != null && customError.invoke(e)) {
            return@catch
        } else {
            contentState.let { contentState ->
                errorHandler.handleRequestError(e) {
                    contentState.value = ContentState.ERROR.apply { contentError = it }
                }
            }
        }
    }

    protected fun <T> Flow<T>.collectWithLaunch(action: (suspend (value: T) -> Unit)? = null) =
        viewModelScope.launch {
            collect { value -> action?.invoke(value) }
        }

    protected fun getString(id: Int): String = theme.value!!.resources!!.getString(id)

    protected fun getString(id: Int, vararg formats: Any): String =
        theme.value!!.resources!!.getString(id, *formats)

}