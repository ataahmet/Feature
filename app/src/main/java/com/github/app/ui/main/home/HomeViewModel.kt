package com.github.app.ui.main.home

import androidx.paging.PagingData
import com.github.app.domain.entity.SearchRepo
import com.github.app.domain.usecase.RepoListDataUseCase
import com.github.app.reduce.Action
import com.github.app.reduce.Change
import com.github.app.reduce.State
import com.github.app.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxkotlin.ofType
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(val repoListDataUseCase: RepoListDataUseCase) :
    BaseViewModel<Action, State>() {
        val searchRepoPagingFlow: Flow<PagingData<SearchRepo>> =
            repoListDataUseCase.getSearchRepoPagingFlow()

        override fun bind() {
            val userDetailRequest =
                actions.ofType<Action.ActionUserDetail>().switchMap {
                    repoListDataUseCase.userDetail(it.ownerName).subscribeOn(Schedulers.io())
                        .map<Change> { Change.Loaded(it) }
                        .onErrorReturn { Change.LoadError(it) }
                        .startWith(Change.Loading)
                }

            disposables +=
                userDetailRequest.scan(initialState, reducer)
                    .distinctUntilChanged()
                    .subscribe(state::postValue, Timber::e)
        }
    }
