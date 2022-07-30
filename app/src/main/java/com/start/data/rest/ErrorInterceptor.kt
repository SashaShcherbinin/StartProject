package com.start.data.rest

import com.google.gson.Gson
import e.palyvo.domain.exeption.ConnectionException
import e.palyvo.domain.exeption.ServerException
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class ErrorInterceptor
@Inject
constructor(val gson: Gson) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var response: Response? = null
        try {
            try {
                response = chain.proceed(chain.request())
                val body = response.peekBody(1048576L).string()
                val code = response.code

                if (code == 500) {
                    throw ServerException(body)
                }
                return response
            } catch (e: ConnectException) {
                Timber.e(e)
                throw ServerException("Server is down")
            } catch (e: UnknownHostException) {
                Timber.e(e)
                throw ConnectionException("Network connection")
            } catch (e: SocketTimeoutException) {
                Timber.e(e)
                throw ConnectionException("Server is down")
            }
        } catch (e: Throwable) {
            if (response != null) {
                updateStackTrace(response.request.url.encodedPath, e, response.code)
            }
            throw e
        }
    }

    /*
     * To show request info in stack trace in crashlytics
     */
    private fun updateStackTrace(requestPath: String, newThrowable: Throwable, httpCode: Int) {
        val element = StackTraceElement(
            "REST=$requestPath",
            "",
            "",
            httpCode
        )
        val elementList = newThrowable.stackTrace.toMutableList()
        elementList.add(0, element)
        newThrowable.stackTrace = elementList.toTypedArray()
    }

}