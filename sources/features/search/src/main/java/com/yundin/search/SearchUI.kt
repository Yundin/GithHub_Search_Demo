package com.yundin.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
    ObserveCustomTabEvents(
        event = viewModel.customTabsEvent,
        onLaunch = viewModel::onIntentLaunched
    )
    Column {
        SearchField(
            value = request,
            onValueChange = viewModel::onInputChange,
            label = "GitHub repository search"
        )
        Divider()
        SearchResults(
            lazyPagingItems = lazyPagingItems,
            onClick = viewModel::onRepositoryClick
        )
        EmptyState(lazyPagingItems = lazyPagingItems)
    }
}

@Composable
private fun SearchResults(
    lazyPagingItems: LazyPagingItems<Repository>,
    onClick: (Repository) -> Unit
) {
    val lazyListState = rememberLazyListState()
    LaunchedEffect(lazyPagingItems.loadState.refresh) {
        // new query, scroll to top
        lazyListState.animateScrollToItem(0, 0)
    }
    LazyColumn(state = lazyListState) {
        val onRetry = { lazyPagingItems.retry() }
        itemProgressAndLoadingForState(lazyPagingItems.loadState.refresh, onRetry)
        itemsRepositories(lazyPagingItems = lazyPagingItems, onClick = onClick)
        itemProgressAndLoadingForState(lazyPagingItems.loadState.append, onRetry)
    }
}

private fun LazyListScope.itemProgressAndLoadingForState(
    loadState: LoadState,
    onRetry: () -> Unit
) {
    item {
        AnimatedVisibilityProgressBarItem(
            visible = loadState is LoadState.Loading
        )
        AnimatedVisibility(
            visible = loadState is LoadState.Error
        ) {
            LoadingFailedItem(onRetryClick = onRetry)
        }
    }
}

private fun LazyListScope.itemsRepositories(
    lazyPagingItems: LazyPagingItems<Repository>,
    onClick: (Repository) -> Unit
) {
    items(lazyPagingItems) { repository ->
        repository?.let {
            val uiRepository = UIRepository(it.fullName, it.description)
            RepositoryCard(repository = uiRepository) {
                onClick(it)
            }
        }
    }
}

@Composable
private fun ObserveCustomTabEvents(event: SearchViewModel.CustomTabEvent?, onLaunch: () -> Unit) {
    val context = LocalContext.current
    LaunchedEffect(event) {
        event?.let {
            it.intent.launchUrl(context, it.uri)
            onLaunch()
        }
    }
}

@Composable
private fun EmptyState(lazyPagingItems: LazyPagingItems<Repository>) {
    val notLoading = lazyPagingItems.loadState.run {
        listOf(refresh, append, prepend).all { it is LoadState.NotLoading }
    }
    if (notLoading && lazyPagingItems.itemCount == 0) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "Nothing to show",
            )
        }
    }
}

@Composable
private fun <T> LiveData<T>.observeAsNonNullState(): State<T> = observeAsState(value!!)
