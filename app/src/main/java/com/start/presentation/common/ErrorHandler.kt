@file:Suppress("WildcardImport")

package com.start.presentation.common

import com.start.R
import com.start.presentation.entity.ContentError
import com.start.presentation.entity.MessageContentError
import com.start.presentation.entity.ResourceStringContentError
import e.palyvo.domain.exeption.ConnectionException
import e.palyvo.domain.exeption.ServerApiException
import e.palyvo.domain.exeption.ServerException
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