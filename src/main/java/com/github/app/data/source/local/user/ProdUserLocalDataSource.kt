package com.github.app.data.source.local.user

import com.github.app.util.Keys
import io.paperdb.Book
import javax.inject.Inject

class ProdUserLocalDataSource @Inject constructor(
    private val defaultBook: Book
) : UserLocalDataSource {
    override fun writeUserName(username: String) {
        defaultBook.write(Keys.USERNAME, username)
    }

    override fun getUserName(): String {
        return defaultBook.read(Keys.USERNAME)
    }
}