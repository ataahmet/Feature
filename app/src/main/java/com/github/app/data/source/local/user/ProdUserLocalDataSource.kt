package com.github.app.data.source.local.user

import com.github.app.util.Keys
import io.paperdb.Book
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProdUserLocalDataSource @Inject constructor(
    private val defaultBook: Book
) : UserLocalDataSource {
    override fun getUser(): Single<String> {
        return Single.just(defaultBook.read(Keys.USERNAME))
    }

    override fun writeUserName(username: String) {
        defaultBook.write(Keys.USERNAME, username)
    }

    override fun getUserName(): String {
        return defaultBook.read(Keys.USERNAME)
    }
}