package com.github.app.ui.main.userdetail

import androidx.paging.PagingData
import com.github.app.domain.entity.SearchRepo
import com.github.app.domain.usecase.UserRepoDataSourceUsace
import com.github.app.reduce.Action
import com.github.app.reduce.State
import com.github.app.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(userRepoDataSourceUsace: UserRepoDataSourceUsace) :
    BaseViewModel<Action, State>() {

    val searchRepoPagingFlow: Flow<PagingData<SearchRepo>> =
        userRepoDataSourceUsace.getUserRepoPagingFlow()

    override fun bind() {}
}
