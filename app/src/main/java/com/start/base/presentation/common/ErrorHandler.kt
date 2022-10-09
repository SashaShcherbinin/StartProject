@file:Suppress("WildcardImport")

package com.start.base.presentation.common

import com.start.R
import com.start.base.presentation.entity.ContentError
import com.start.base.presentation.entity.MessageContentError
import com.start.base.presentation.entity.ResourceStringContentError
import com.start.base.domain.exeption.ConnectionException
import com.start.base.domain.exeption.ServerApiException
import com.start.base.domain.exeption.ServerException
import timber.log.Timber

open class ErrorHandler {

    fun handleRequestError(e: Throwable, handler: ((ContentError) -> Unit)? = null) {
        when (e) {
            is ConnectionException -> Timber.w(e)
            else -> Timber.e(e)
        }
        when (e) {
            is ConnectionException -> {
                handler?.invoke(ResourceStringContentError(R.string.general_error_networkConnection))
            }
            is ServerException -> {
                handler?.invoke(ResourceStringContentError(R.string.general_error_serverError))
            }
            is ServerApiException -> {
                handler?.invoke(MessageContentError(e.userMessage))
            }
            else -> {
                handler?.invoke(
                    ResourceStringContentError(R.string.general_error_serverError)
                )
            }
        }
    }
}