package com.start.presentation.widget.error

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.start.R

class CommonErrorView(context: Context, attrs: AttributeSet? = null) :
    ConstraintLayout(context, attrs) {

    init {
        LayoutInflater.from(context)
            .inflate(R.layout.view_error_default, this, true)
    }

    fun setRetryListener(callback: () -> Unit) =
        findViewById<Button>(R.id.retryBtn).setOnClickListener {
            callback()
        }

    fun setMessage(message: String) {
        findViewById<TextView>(R.id.errorMessageTv).text = message
    }

}