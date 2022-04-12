package com.yundin.core.repository

import androidx.paging.PagingData
import com.yundin.core.model.GithubRepository
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun search(query: String): Flow<PagingData<GithubRepository>>
}
