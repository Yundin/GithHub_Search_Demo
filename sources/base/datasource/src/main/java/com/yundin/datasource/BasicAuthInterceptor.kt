package com.yundin.datasource

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response

class BasicAuthInterceptor(
    login: String,
    password: String
) : Interceptor {
    private val credentials = Credentials.basic(login, password)
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.request()
            .newBuilder()
            .header("Authorization", credentials)
            .build()
            .let {
                chain.proceed(it)
            }
    }
}