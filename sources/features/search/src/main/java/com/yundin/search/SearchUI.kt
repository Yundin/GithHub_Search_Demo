package com.yundin.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.yundin.navigation.SearchScreens
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
fun NavGraphBuilder.SearchComposable() {
    composable(SearchScreens.Search.destination) {
        val viewModel: SearchViewModel = viewModel()
        SearchUI(viewModel = viewModel)
    }
}

@ExperimentalCoroutinesApi
@FlowPreview
@Composable
fun SearchUI(viewModel: SearchViewModel) {
    val lazyPagingItems = viewModel.searchResult.collectAsLazyPagingItems()
    val request: String by viewModel.searchRequest.observeAsNonNullState()
    Column {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = request,
            onValueChange = viewModel::onInputChange
        )
        LazyColumn {
            items(lazyPagingItems) {
                Column {
                    Text("Name is ${it?.fullName}")
                    Text("Desc is ${it?.description}")
                }
            }
        }
    }
}

@Composable
fun <T> LiveData<T>.observeAsNonNullState(): State<T> = observeAsState(value!!)
