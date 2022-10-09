package com.start.base.presentation.entity

import android.content.Context

data class MessageContentError(val message: String) : ContentError() {

    override fun handleMessage(
        context: Context,
        messageHandler: ((String) -> Unit)
    ) = messageHandler(message)

}