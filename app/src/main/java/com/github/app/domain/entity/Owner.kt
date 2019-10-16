package com.github.app.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Owner(
    val ownerImageUrl: String?,
    val ownerName: String?,
    val email: String?
) : Entity, Parcelable