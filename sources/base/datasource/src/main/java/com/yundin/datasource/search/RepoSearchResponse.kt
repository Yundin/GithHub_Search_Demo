package com.yundin.datasource.search

import com.google.gson.annotations.SerializedName
import com.yundin.core.model.GithubRepository

data class RepoSearchResponse(
    @SerializedName("total_count")
    val totalCount: Int,
    @SerializedName("items")
    val items: List<Repository>,
)

data class Repository(
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("html_url")
    val htmlUrl: String,
) {
    fun toDomain(): GithubRepository = GithubRepository(fullName, description, htmlUrl)
}
