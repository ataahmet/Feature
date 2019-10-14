package com.github.app.data.source.remote.api.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OwnerItem(
    val `avatar_url`: String?,
    val `login`: String?,
    val `blog`: String?
) : ApiEntity