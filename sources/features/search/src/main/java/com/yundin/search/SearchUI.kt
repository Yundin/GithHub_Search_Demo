package com.yundin.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.yundin.datasource.Repository
import com.yundin.designsystem.theme.components.*
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
        SearchField(value = request, onValueChange = viewModel::onInputChange, label = "GitHub repository search")
        Divider()
        SearchResults(lazyPagingItems = lazyPagingItems)
    }
}

@Composable
private fun SearchResults(
    lazyPagingItems: LazyPagingItems<Repository>
) {
    val lazyListState = rememberLazyListState()
    LaunchedEffect(lazyPagingItems.loadState.refresh) {
        // new query, scroll to top
        lazyListState.scrollToItem(0, 0)
    }
    LazyColumn(state = lazyListState) {
        itemProgressBarForState(lazyPagingItems.loadState.refresh)
        itemsRepositories(lazyPagingItems)
        itemProgressBarForState(lazyPagingItems.loadState.append)
        itemLoadingError(lazyPagingItems)
    }
}

private fun LazyListScope.itemProgressBarForState(loadState: LoadState) {
    item {
        AnimatedVisibilityProgressBarItem(
            visible = loadState is LoadState.Loading
        )
    }
}

private fun LazyListScope.itemLoadingError(lazyPagingItems: LazyPagingItems<Repository>) {
    item {
        val refreshFailed = lazyPagingItems.loadState.refresh is LoadState.Error
        val appendFailed = lazyPagingItems.loadState.append is LoadState.Error
        AnimatedVisibility(
            visible = refreshFailed || appendFailed
        ) {
            LoadingFailedItem { lazyPagingItems.retry() }
        }
    }
}

private fun LazyListScope.itemsRepositories(
    lazyPagingItems: LazyPagingItems<Repository>
) {
    items(lazyPagingItems) { repository ->
        repository?.let {
            val uiRepository = UIRepository(it.fullName, it.description)
            RepositoryCard(repository = uiRepository) {
                // click
            }
        }
    }

}

@Composable
private fun <T> LiveData<T>.observeAsNonNullState(): State<T> = observeAsState(value!!)
