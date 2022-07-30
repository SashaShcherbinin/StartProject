@file:Suppress("unused")

package com.start.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.start.R
import com.start.presentation.extension.gone
import com.start.presentation.extension.visible
import e.palyvo.presentation.common.entity.ContentState

class ContentStateLayout(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private var emptyId: Int?

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ContentStateLayout)
        emptyId = a.getResourceId(R.styleable.ContentStateLayout_csl_empty_id, -1)
            .takeIf { it != -1 }
        a.recycle()
    }

    private var loadingCreated: Boolean = false
    private var errorCreated: Boolean = false
    private var emptyCreated: Boolean = false

    private val loadingView: View by lazy {
        loadingCreated = true
        LayoutInflater.from(context)
            .inflate(R.layout.general_content_state_loading, this, false)
            .apply { addView(this) }
    }
    private val errorView: View by lazy {
        errorCreated = true
        LayoutInflater.from(context)
            .inflate(R.layout.general_content_state_error, this, false)
            .apply { addView(this) }
    }
    private val emptyView: View by lazy {
        emptyCreated = true
        findViewById(emptyId!!)
    }
    private val contentView: View by lazy {
        for (i in 0 until childCount) {
            getChildAt(i).let { v ->
                if (loadingCreated && loadingView == v) null
                else if (errorCreated && errorView == v) null
                else if (emptyCreated && emptyView == v) null
                else return@lazy v
            } ?: continue
        }
        error("could not find a content view")
    }

    var retryCallbackContentState: (() -> Unit)? = null

    fun setContentState(contentState: ContentState) {
        retryCallbackContentState!!
        when (contentState) {
            ContentState.CONTENT -> {
                contentView.visible()
                if (loadingCreated) loadingView.gone()
                if (errorCreated) errorView.gone()
                if (emptyCreated) emptyView.gone()
            }
            ContentState.LOADING -> {
                contentView.gone()
                loadingView.visible()
                if (errorCreated) errorView.gone()
                if (emptyCreated) emptyView.gone()
            }
            ContentState.ERROR -> {
                contentView.gone()
                if (loadingCreated) loadingView.gone()
                errorView.visible()
                if (emptyCreated) emptyView.gone()
                errorView.findViewById<ErrorContainerView>(R.id.errorContainerView)
                    .apply {
                        retryCallback = retryCallbackContentState!!
                        setErrorView(contentState.contentError!!)
                    }
            }
            ContentState.EMPTY -> {
                contentView.gone()
                if (loadingCreated) loadingView.gone()
                if (errorCreated) errorView.gone()
                emptyView.visible()
            }
        }
    }
}