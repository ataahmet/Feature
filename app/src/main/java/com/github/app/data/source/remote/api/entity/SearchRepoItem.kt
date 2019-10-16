package com.github.app.data.source.remote.api.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchRepoItem(
    val `items`: List<RepoItem?>?
) : ApiEntity {
    @JsonClass(generateAdapter = true)
    data class RepoItem(
        val `id`: Int,
        val `name`: String?,
        val `forks`: Int,
        val `owner`: OwnerItem?

    ) : ApiEntity
}