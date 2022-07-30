package com.start.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.start.presentation.entity.ContentError
import com.start.presentation.entity.RetryEvent

class ErrorContainerView(context: Context, attrs: AttributeSet?) :
    FrameLayout(context, attrs) {

    var retryCallback: (() -> Unit)? = null

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        postDelayed({
            if (retryCallback == null) error("You forgot set retry callback")
        }, 2000)
    }

    fun setErrorView(contentError: ContentError) {
        contentError.handleView(
            context = context,
            viewHandler = {
                removeAllViews()
                it.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
                addView(it)
                object : RetryEvent {
                    override fun retry() = retryCallback!!()
                }
            })
    }

}