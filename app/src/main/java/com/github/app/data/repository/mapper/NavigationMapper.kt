package com.github.app.data.repository.mapper

import com.github.app.domain.entity.NavigationItem
import com.github.app.helper.NavController.Companion.githup

fun mapToEntity(array: Array<String>): List<NavigationItem> {
    var navItems = mutableListOf<NavigationItem>()
    array.toList().forEachIndexed { index, element ->
        navItems.add(NavigationItem(index, element, false, index == githup))
    }
    return navItems
}
