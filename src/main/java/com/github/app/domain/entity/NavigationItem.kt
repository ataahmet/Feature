package com.github.app.domain.entity

data class NavigationItem(
    val id: Int,
    val value: String,
    var showDialog: Boolean,
    var isSelected: Boolean
) : Entity