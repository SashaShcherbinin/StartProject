@file:Suppress("unused")

package com.start.base.presentation.binding

import androidx.databinding.BindingAdapter
import com.start.base.presentation.widget.ContentStateLayout
import e.palyvo.presentation.common.entity.ContentState

object CustomBindingAdapter {

    @JvmStatic
    @BindingAdapter(value = ["bind_csl_onRetry", "bind_csl_contentState"], requireAll = false)
    fun setListener(
        containerView: ContentStateLayout,
        callback: (() -> Unit)?,
        contentState: ContentState?,
    ) {
        containerView.retryCallbackContentState = callback!!
        contentState?.let { containerView.setContentState(it) }
    }
}