package com.yundin.search

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.yundin.datasource.Network
import com.yundin.datasource.RepoSearchPagingSource
import com.yundin.datasource.Repository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest

class SearchViewModel : ViewModel() {

    private val _searchRequest = MutableLiveData("")
    val searchRequest: LiveData<String> = _searchRequest
    @FlowPreview
    @ExperimentalCoroutinesApi
    val searchResult: Flow<PagingData<Repository>> = searchRequest
        .asFlow()
        .filter { it.isNotBlank() }
        .debounce(SEARCH_DEBOUNCE_DELAY)
        .flatMapLatest {
            Pager(PagingConfig(30)) {
                RepoSearchPagingSource(Network.getApi(), it)
            }.flow
        }
        .cachedIn(viewModelScope)

    fun onInputChange(text: String) {
        _searchRequest.value = text
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 300L
    }
}
