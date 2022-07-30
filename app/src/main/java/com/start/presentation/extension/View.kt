package com.start.presentation.extension

import android.view.View

fun View.gone() {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
}

fun View.visible() {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
}

fun View.animateVisible(duration: Long = 100) {
    alpha = 0f
    visible()
    animate().setDuration(duration).alpha(1f).withEndAction { alpha = 1f }
}

fun View.animateGone(duration: Long = 100) {
    animate().setDuration(duration).alpha(0f).withEndAction {
        visibility = View.GONE
    }
}

inline fun View.onClick(crossinline f: () -> Unit) {
    setOnClickListener { f() }
}