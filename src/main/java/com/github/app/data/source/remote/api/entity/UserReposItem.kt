package com.github.app.data.source.remote.api.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserReposItem(
    val `id`: Int,
    val `name`: String?,
    val `owner`: OwnerItem?,
    val `forks`: Int
) : ApiEntity
