package com.yundin.datasource.search

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.yundin.core.model.GithubRepository
import com.yundin.core.repository.SearchRepository
import com.yundin.datasource.api.GithubApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val githubApi: GithubApi
) : SearchRepository {

    override fun search(query: String): Flow<PagingData<GithubRepository>> {
        return Pager(PagingConfig(30)) {
            RepoSearchPagingSource(githubApi, query)
        }
            .flow
            .map { pagingData ->
                pagingData.map { it.toDomain() }
            }
    }
}
