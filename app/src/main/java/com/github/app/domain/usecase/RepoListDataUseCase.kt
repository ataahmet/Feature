package com.github.app.domain.usecase

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.github.app.data.datasource.ProdSearchRepoListDataSource
import com.github.app.data.source.remote.api.GithubApi
import com.github.app.domain.entity.Owner
import com.github.app.domain.entity.SearchRepo
import com.github.app.domain.repository.UserRepository
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class RepoListDataUseCase @Inject constructor(
    private var githubApi: GithubApi,
    private var userRepository: UserRepository
) :
    DataSource.Factory<Int, SearchRepo>(), SearchRepoDataSourceUsace {

    override fun userCache(): Observable<String> {
        return userRepository.getUser().toObservable()
    }

    val searchRepoDataSourceLiveData = MutableLiveData<ProdSearchRepoListDataSource>()

    private lateinit var componentDisposable: CompositeDisposable


    override fun userDetail(ownerName: String): Observable<Owner> {
        return userRepository.getUserDetail(ownerName).toObservable()
    }

    override fun initSearchRepoUsacase(mCompositeDisposable: CompositeDisposable) {
        componentDisposable = mCompositeDisposable
    }

    override fun create(): DataSource<Int, SearchRepo> {
        val searchRepoListDataSource = ProdSearchRepoListDataSource(githubApi, componentDisposable)
        searchRepoDataSourceLiveData.postValue(searchRepoListDataSource)
        return searchRepoListDataSource
    }
}