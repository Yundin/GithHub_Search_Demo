package com.yundin.search

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.yundin.core.App
import com.yundin.core.model.GithubRepository
import com.yundin.designsystem.theme.components.*
import com.yundin.navigation.SearchScreens
import com.yundin.search.di.DaggerSearchComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalComposeUiApi
@ExperimentalCoroutinesApi
@ExperimentalAnimationApi
@FlowPreview
fun NavGraphBuilder.SearchComposable() {
    composable(SearchScreens.Search.destination) {
        val app = LocalContext.current.applicationContext as App
        val viewModel: SearchViewModel = viewModel(
            factory = object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return DaggerSearchComponent.builder()
                        .searchDependencies(app.getAppProvider())
                        .build()
                        .getSearchViewModel() as T
                }
            }
        )
        SearchUI(viewModel = viewModel)
    }
}

@ExperimentalComposeUiApi
@ExperimentalCoroutinesApi
@ExperimentalAnimationApi
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
            label = stringResource(R.string.repository_search_field_hint)
        )
        Divider()
        SearchResults(
            lazyPagingItems = lazyPagingItems,
            onClick = viewModel::onRepositoryClick
        )
        EmptyState(lazyPagingItems = lazyPagingItems)
    }
}

@ExperimentalAnimationApi
@Composable
private fun SearchResults(
    lazyPagingItems: LazyPagingItems<GithubRepository>,
    onClick: (GithubRepository) -> Unit
) {
    // resets on conf changes, bug: https://issuetracker.google.com/issues/179397301
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

@ExperimentalAnimationApi
private fun LazyListScope.itemProgressAndLoadingForState(
    loadState: LoadState,
    onRetry: () -> Unit
) {
    item {
        AnimatedContent(targetState = loadState) {
                if (it is LoadState.Loading) {
                    ProgressBarItem()
                }
                if (it is LoadState.Error) {
                    LoadingFailedItem(onRetryClick = onRetry)
                }
        }
    }
}

private fun LazyListScope.itemsRepositories(
    lazyPagingItems: LazyPagingItems<GithubRepository>,
    onClick: (GithubRepository) -> Unit
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
private fun EmptyState(lazyPagingItems: LazyPagingItems<GithubRepository>) {
    val notLoading = lazyPagingItems.loadState.run {
        listOf(refresh, append, prepend).all { it is LoadState.NotLoading }
    }
    if (notLoading && lazyPagingItems.itemCount == 0) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.repository_search_empty_message),
            )
        }
    }
}

@Composable
private fun <T> LiveData<T>.observeAsNonNullState(): State<T> = observeAsState(value!!)
