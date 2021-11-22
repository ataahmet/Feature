package com.github.app.ui.main.home

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.github.app.domain.entity.SearchRepo
import com.github.app.domain.usecase.RepoListDataUseCase
import com.github.app.reduce.Action
import com.github.app.reduce.Change
import com.github.app.reduce.State
import com.github.app.ui.base.BaseViewModel
import com.github.app.util.Keys
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxkotlin.ofType
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val repoListDataUseCase: RepoListDataUseCase) :
    BaseViewModel<Action, State>() {

    var liveDataSearchRepoList: LiveData<PagedList<SearchRepo>>

    private val perPage: Int = Keys.PER_PAGE


    init {
        repoListDataUseCase.initSearchRepoUsacase(disposables)
        val config = PagedList.Config.Builder()
            .setPageSize(perPage)
            .setInitialLoadSizeHint(perPage * 2)
            .setEnablePlaceholders(false)
            .build()
        liveDataSearchRepoList = LivePagedListBuilder(repoListDataUseCase, config).build()

    }

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