package com.github.app.domain.usecase

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.github.app.data.datasource.ProdUserRepoListDataSource
import com.github.app.data.source.remote.api.GithubApi
import com.github.app.domain.entity.SearchRepo
import com.github.app.domain.repository.UserRepository
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class UserRepoDataSourceUsace @Inject constructor(
    private var githubApi: GithubApi,
    private var userRepository: UserRepository
) : DataSource.Factory<Int, SearchRepo>(), UserRepoListDataSourceUsace {

    private lateinit var componentDisposable: CompositeDisposable

    val searchRepoDataSourceLiveData = MutableLiveData<ProdUserRepoListDataSource>()

    override fun initSearchRepoUsacase(mCompositeDisposable: CompositeDisposable) {
        componentDisposable = mCompositeDisposable
    }

    override fun create(): DataSource<Int, SearchRepo> {
        val searchRepoListDataSource =
            ProdUserRepoListDataSource(githubApi, componentDisposable, userRepository)
        searchRepoDataSourceLiveData.postValue(searchRepoListDataSource)
        return searchRepoListDataSource
    }
}