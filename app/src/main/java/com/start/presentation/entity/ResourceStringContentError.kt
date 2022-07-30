package com.start.presentation.entity

import android.content.Context
import androidx.annotation.StringRes

data class ResourceStringContentError(@StringRes val resId: Int) : ContentError() {

    override fun handleMessage(
        context: Context,
        messageHandler: ((String) -> Unit)
    ) = messageHandler(context.getString(resId))

}