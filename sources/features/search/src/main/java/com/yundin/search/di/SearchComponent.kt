package com.yundin.search.di

import com.yundin.search.SearchViewModel
import dagger.Component

@Component
interface SearchComponent {

    fun getSearchViewModel(): SearchViewModel
}