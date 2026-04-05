package com.github.app.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.github.app.data.datasource.SearchRepoPagingSource
import com.github.app.data.source.remote.api.GithubApi
import com.github.app.domain.entity.Owner
import com.github.app.domain.entity.SearchRepo
import com.github.app.domain.repository.UserRepository
import com.github.app.util.Keys
import dagger.hilt.android.scopes.ViewModelScoped
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class RepoListDataUseCase @Inject constructor(
    private var githubApi: GithubApi,
    private var userRepository: UserRepository
) : SearchRepoDataSourceUsace {

    override fun userCache(): Observable<String> {
        return userRepository.getUser().toObservable()
    }

    override fun userDetail(ownerName: String): Observable<Owner> {
        return userRepository.getUserDetail(ownerName).toObservable()
    }

    override fun initSearchRepoUsacase(mCompositeDisposable: CompositeDisposable) {
        // Paging 3 ile disposable yönetimi PagingSource içinde yapılıyor
    }

    fun getSearchRepoPagingFlow(): Flow<PagingData<SearchRepo>> {
        return Pager(
            config = PagingConfig(
                pageSize = Keys.PER_PAGE,
                initialLoadSize = Keys.PER_PAGE * 2,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { SearchRepoPagingSource(githubApi) }
        ).flow
    }
}
