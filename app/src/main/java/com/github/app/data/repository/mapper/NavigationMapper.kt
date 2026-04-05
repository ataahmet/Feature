package com.github.app.data.repository.mapper

import com.github.app.domain.entity.NavigationItem

fun mapToEntity(array: Array<String>): List<NavigationItem> {
    var navItems = mutableListOf<NavigationItem>()
    array.toList().forEachIndexed { index, element ->
        navItems.add(NavigationItem(index, element, false, index == 0))
    }
    return navItems
}
