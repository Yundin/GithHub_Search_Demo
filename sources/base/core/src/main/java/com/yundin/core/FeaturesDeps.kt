package com.yundin.core

import com.yundin.core.repository.SearchRepository

interface SearchDependencies {
    fun getSearchRepository(): SearchRepository
}
