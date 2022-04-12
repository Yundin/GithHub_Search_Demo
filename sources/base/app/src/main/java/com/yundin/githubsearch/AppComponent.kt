package com.yundin.githubsearch

import android.app.Application
import com.yundin.core.ApplicationProvider
import com.yundin.core.scope.AppScope
import com.yundin.datasource.di.NetworkModule
import dagger.Component

@[AppScope Component(modules = [NetworkModule::class], dependencies = [Application::class])]
interface AppComponent : ApplicationProvider
