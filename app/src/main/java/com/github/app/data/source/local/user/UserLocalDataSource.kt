package com.github.app.data.source.local.user

import com.github.app.data.source.local.LocalDataSource
import io.reactivex.Single

interface UserLocalDataSource : LocalDataSource {
    fun writeUserName(username: String)
    fun getUserName(): String
    fun getUser(): Single<String>
}