package com.github.app.data.source.navigation

interface NavigationDataSource {
    fun getNavigationItems(): Array<String>
}