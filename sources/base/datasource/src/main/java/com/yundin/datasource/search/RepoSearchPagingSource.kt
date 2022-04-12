package com.yundin.datasource.search

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yundin.datasource.api.GithubApi
import retrofit2.HttpException
import java.io.IOException

class RepoSearchPagingSource(
    private val api: GithubApi,
    private val query: String
): PagingSource<Int, Repository>() {
    private var itemsLoaded = 0
    private var totalCount: Int? = null
    override fun getRefreshKey(state: PagingState<Int, Repository>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repository> {
        val loadSize = params.loadSize.coerceIn(1..100)
        val nextPageNumber = itemsLoaded / loadSize + 1
        try {
            val response = api.searchRepositories(query, loadSize, nextPageNumber)
            totalCount = response.totalCount
            itemsLoaded += response.items.count()
            return LoadResult.Page(
                data = response.items,
                prevKey = null, // Only paging forward.
                nextKey = if (itemsLoaded < totalCount!!) nextPageNumber + 1 else null
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}
