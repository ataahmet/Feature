package com.github.app.domain.repository

interface RepoListDataSource : Repository {
    fun retry()
}