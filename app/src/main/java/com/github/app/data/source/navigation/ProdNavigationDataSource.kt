package com.github.app.data.source.navigation

import io.paperdb.Book
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProdNavigationDataSource @Inject constructor(
    private val defaultBook: Book
) : NavigationDataSource {
    override fun getNavigationItems(): Array<String> {
        return arrayOf("List of Search Repository", "Log Out ")
    }
}