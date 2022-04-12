package com.yundin.datasource.di

import com.yundin.core.repository.SearchRepository
import com.yundin.core.scope.AppScope
import com.yundin.datasource.Network
import com.yundin.datasource.api.GithubApi
import com.yundin.datasource.search.SearchRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface NetworkModule {
    @[AppScope Binds]
    fun bindSearchRepository(repository: SearchRepositoryImpl): SearchRepository

    companion object {
        @[AppScope Provides]
        fun provideGithubApi(): GithubApi = Network.createApi()
    }
}
