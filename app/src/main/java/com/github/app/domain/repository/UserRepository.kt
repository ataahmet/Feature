package com.github.app.domain.repository

import com.github.app.domain.entity.Owner
import io.reactivex.Single

interface UserRepository : Repository {
    fun getUserName(): String
    fun getUser(): Single<String>
    fun getUserDetail(ownerName: String): Single<Owner>
}