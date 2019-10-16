package com.github.app.ui.main.userdetail

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.github.app.domain.entity.SearchRepo
import com.github.app.domain.usecase.UserRepoDataSourceUsace
import com.github.app.reduce.Action
import com.github.app.reduce.State
import com.github.app.ui.base.BaseViewModel
import com.github.app.util.Keys
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class UserDetailViewModel @Inject constructor(userRepoDataSourceUsace: UserRepoDataSourceUsace) :
    BaseViewModel<Action, State>() {



    var liveDataUserRepoList: LiveData<PagedList<SearchRepo>>
    private val compositeDisposable: CompositeDisposable = CompositeDisposable();
    private val perPage: Int = Keys.PER_PAGE

    override val initialState = State(isIdle = true)


    override fun bind() {

    }

    init {
        userRepoDataSourceUsace.initSearchRepoUsacase(compositeDisposable)
        val config = PagedList.Config.Builder()
            .setPageSize(perPage)
            .setInitialLoadSizeHint(perPage * 2)
            .setEnablePlaceholders(false)
            .build()
        liveDataUserRepoList = LivePagedListBuilder(userRepoDataSourceUsace, config).build()
    }
}