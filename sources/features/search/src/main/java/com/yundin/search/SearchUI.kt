package com.yundin.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.yundin.navigation.SearchScreens

fun NavGraphBuilder.SearchComposable() {
    composable(SearchScreens.Search.destination) {
        val viewModel: SearchViewModel = viewModel()
        SearchUI(viewModel = viewModel)
    }
}

@Composable
fun SearchUI(viewModel: SearchViewModel) {
    val name: List<String> by viewModel.searchResult.collectAsState()
    val request: String by viewModel.searchRequest.collectAsState()
    Column {
        Text(text = "Hello $name!")
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = request,
            onValueChange = viewModel::onInputChange
        )
    }
}
