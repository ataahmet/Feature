package com.github.app.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchRepo(
    val id: Int,
    val repoName: String?,
    val owner: Owner?,
    val forks: Int
) : Entity, Parcelable
