package com.github.app.domain.usecase

import io.reactivex.disposables.CompositeDisposable

interface UserRepoListDataSourceUsace {
    fun initSearchRepoUsacase(mCompositeDisposable: CompositeDisposable)
}