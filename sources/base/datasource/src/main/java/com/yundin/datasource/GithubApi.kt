package com.yundin.datasource

import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApi {
    @GET("search/repositories")
    suspend fun searchRepositories(
        @Query("q") query: String,
        @Query("per_page") loadSize: Int,
        @Query("page") page: Int,
    ): RepoSearchResponse
}