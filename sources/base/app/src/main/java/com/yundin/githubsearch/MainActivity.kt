package com.yundin.githubsearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.yundin.designsystem.theme.GithubSearchTheme
import com.yundin.navigation.SearchScreens
import com.yundin.search.SearchComposable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalComposeUiApi
@ExperimentalCoroutinesApi
@ExperimentalAnimationApi
@FlowPreview
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_GithubSearch)
        setContent {
            GithubSearchTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NavHost()
                }
            }
        }
    }
}

@ExperimentalComposeUiApi
@ExperimentalCoroutinesApi
@ExperimentalAnimationApi
@FlowPreview
@Composable
private fun NavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = SearchScreens.Search.destination
    ) {
        SearchComposable()
    }
}
