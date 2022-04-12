package com.yundin.search

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.yundin.core.model.GithubRepository
import com.yundin.core.repository.SearchRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val _searchRequest = MutableLiveData("")
    val searchRequest: LiveData<String> = _searchRequest

    @FlowPreview
    @ExperimentalCoroutinesApi
    val searchResult: Flow<PagingData<GithubRepository>> = searchRequest
        .asFlow()
        .debounce(SEARCH_DEBOUNCE_DELAY)
        .filter { it.isNotBlank() }
        .flatMapLatest {
            searchRepository.search(it)
        }
        .cachedIn(viewModelScope)

    var customTabsEvent by mutableStateOf<CustomTabEvent?>(null)
        private set


    fun onInputChange(text: String) {
        _searchRequest.value = text
    }

    fun onRepositoryClick(repository: GithubRepository) {
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
