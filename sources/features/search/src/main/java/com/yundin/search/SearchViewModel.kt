package com.yundin.search

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
        .debounce(SEARCH_DEBOUNCE_DELAY)
        .filter { it.isNotBlank() }
        .flatMapLatest {
            Pager(PagingConfig(30)) {
                RepoSearchPagingSource(Network.getApi(), it)
            }.flow
        }
        .cachedIn(viewModelScope)

    var customTabsEvent by mutableStateOf<CustomTabEvent?>(null)
        private set


    fun onInputChange(text: String) {
        _searchRequest.value = text
    }

    fun onRepositoryClick(repository: Repository) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsEvent = CustomTabEvent(customTabsIntent, Uri.parse(repository.htmlUrl))
    }

    fun onIntentLaunched() {
        customTabsEvent = null
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 300L
    }

    data class CustomTabEvent(val intent: CustomTabsIntent, val uri: Uri)
}
