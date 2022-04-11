package com.yundin.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private val _searchRequest = MutableStateFlow("")
    val searchRequest: StateFlow<String> = _searchRequest.asStateFlow()
    private val _searchResult = MutableStateFlow(listOf<String>())
    val searchResult = _searchResult.asStateFlow()

    fun onInputChange(text: String) {
        _searchRequest.value = text
        onTextChange(text)
    }

    private val onTextChange = debouncedSearch()

    private fun debouncedSearch(): (String) -> Unit {
        var debounceJob: Job? = null
        var searchJob: Job? = null
        return { param: String ->
            debounceJob?.cancel()
            debounceJob = viewModelScope.launch {
                delay(SEARCH_DEBOUNCE_DELAY)
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    _searchResult.value = listOf("$param started")
                    delay(1000L)
                    _searchResult.value = listOf("$param loaded")
                }
            }
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 300L
    }
}