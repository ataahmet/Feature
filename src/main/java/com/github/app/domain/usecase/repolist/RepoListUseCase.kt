package com.github.app.domain.usecase.repolist

import com.github.app.domain.entity.SearchRepo
import com.github.app.domain.usecase.UseCase
import io.reactivex.Observable

interface RepoListUseCase : UseCase {
    fun repoList(): Observable<List<SearchRepo>>
}