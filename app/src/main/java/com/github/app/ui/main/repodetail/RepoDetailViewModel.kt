package com.github.app.ui.main.repodetail

import com.github.app.domain.usecase.RepoListDataUseCase
import com.github.app.reduce.Action
import com.github.app.reduce.Change
import com.github.app.reduce.Reducer
import com.github.app.reduce.State
import com.github.app.ui.base.BaseViewModel
import io.reactivex.rxkotlin.ofType
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class RepoDetailViewModel @Inject constructor(private val repoListDataUseCase: RepoListDataUseCase) :
    BaseViewModel<Action, State>() {
    override val initialState = State(isIdle = true)

     override fun bind() {
        val userDetailRequest = actions.ofType<Action.ActionUserDetail>().switchMap {
            repoListDataUseCase.userDetail(it.ownerName).subscribeOn(Schedulers.io())
                .map<Change> { Change.Loaded(it) }
                .onErrorReturn { Change.LoadError(it) }
                .startWith(Change.Loading)
        }

        disposables += userDetailRequest.scan(initialState, reducer)
            .distinctUntilChanged()
            .subscribe(state::postValue, Timber::e)
    }
}