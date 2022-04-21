package com.yundin.datasource.utils

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response

class BasicAuthInterceptor(
    login: String,
    private val password: String
) : Interceptor {
    private val credentials = Credentials.basic(login, password)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = if (password.isNotBlank()) {
            chain.request()
                .newBuilder()
                .header("Authorization", credentials)
                .build()
        } else {
            chain.request()
        }
        return chain.proceed(request)
    }
}