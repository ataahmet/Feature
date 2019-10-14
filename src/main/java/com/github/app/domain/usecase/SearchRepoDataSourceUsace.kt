package com.github.app.domain.usecase

import com.github.app.domain.entity.Owner
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

interface SearchRepoDataSourceUsace : UseCase {
    fun initSearchRepoUsacase(mCompositeDisposable: CompositeDisposable)
    fun userDetail(ownerName: String): Observable<Owner>
}