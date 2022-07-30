package com.start.presentation.entity

import android.content.Context
import android.view.View
import com.start.presentation.widget.error.CommonErrorView

abstract class ContentError {

    abstract fun handleMessage(
        context: Context,
        messageHandler: ((String) -> Unit),
    )

    open fun handleView(
        context: Context,
        viewHandler: ((View) -> RetryEvent)
    ) {
        val errorView = CommonErrorView(context)
        val retryEvent = viewHandler(errorView)
        handleMessage(context, errorView::setMessage)
        errorView.setRetryListener { retryEvent.retry() }
    }
}

interface RetryEvent {
    fun retry()
}