package com.github.app.domain.usecase.repolist

import com.github.app.domain.entity.SearchRepo
import com.github.app.domain.repository.SearchRepository
import io.reactivex.Observable
import javax.inject.Inject

class ProdSearchListUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) : RepoListUseCase {
    override fun repoList(): Observable<List<SearchRepo>> {
        return Observable.just(null)
    }
}