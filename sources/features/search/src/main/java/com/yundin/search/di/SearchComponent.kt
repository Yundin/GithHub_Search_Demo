package com.yundin.search.di

import com.yundin.core.SearchDependencies
import com.yundin.core.scope.FeatureScope
import com.yundin.search.SearchViewModel
import dagger.Component

@[FeatureScope Component(dependencies = [SearchDependencies::class])]
internal interface SearchComponent {

    fun getSearchViewModel(): SearchViewModel
}
