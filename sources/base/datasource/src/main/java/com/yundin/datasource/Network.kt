package com.yundin.datasource

import com.yundin.datasource.api.GithubApi
import com.yundin.datasource.utils.BasicAuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


internal class Network {
    companion object {
        fun createApi(): GithubApi {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            }
            val authInterceptor = BasicAuthInterceptor("yundin", "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")
            val client: OkHttpClient = OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(loggingInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(GithubApi::class.java)
        }
    }
}