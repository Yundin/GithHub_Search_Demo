package com.yundin.githubsearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.yundin.githubsearch.ui.theme.GithubSearchTheme
import com.yundin.navigation.SearchScreens
import com.yundin.search.SearchComposable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
