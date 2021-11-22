package com.github.app.data.repository

import com.github.app.data.source.remote.api.GithubApi
import com.github.app.domain.repository.SearchRepository
import javax.inject.Inject

class ProdSearchRepository @Inject constructor(private val githubApi: GithubApi) :
    SearchRepository {
}