package com.github.app.data.repository

import com.github.app.data.repository.mapper.mapToOwnerEntity
import com.github.app.data.source.local.user.UserLocalDataSource
import com.github.app.data.source.remote.api.GithubApi
import com.github.app.domain.entity.Owner
import com.github.app.domain.repository.UserRepository
import io.reactivex.Single
import javax.inject.Inject

class ProdUserRepository @Inject constructor(
    private val userLocalDataSource: UserLocalDataSource,
    private val githubApi: GithubApi
) : UserRepository {

    override fun getUserDetail(ownerName: String): Single<Owner> {
        return githubApi.getUserDetail(ownerName).map {
            userLocalDataSource.writeUserName(ownerName)
            return@map it.mapToOwnerEntity()
        }
    }

    override fun getUserName(): String {
        return userLocalDataSource.getUserName()
    }
}