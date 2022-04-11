package com.yundin.navigation

sealed class SearchScreens(val destination: String) {
    object Search : SearchScreens("search")
}
