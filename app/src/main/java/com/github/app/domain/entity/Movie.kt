package com.github.app.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Repo(
    val id: Int?,
    val posterPath: String?,
    val overview: String?,
    val title: String?

) : Entity, Parcelable