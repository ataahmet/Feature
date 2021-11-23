package com.github.app.domain.usecase

import com.github.app.domain.usecase.repolist.ProdSearchListUseCase
import com.github.app.domain.usecase.repolist.RepoListUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
abstract class UseCaseModule {

    @Singleton
    @Binds
    abstract fun bindRepoListDataSourceFactory(repoListDataSourceUsace: RepoListDataUseCase): SearchRepoDataSourceUsace

    @Singleton
    @Binds
    abstract fun bindUserRepoDataSourceFactory(userRepoDataSourceUsace: UserRepoDataSourceUsace): UserRepoListDataSourceUsace

    @Singleton
    @Binds
    abstract fun bindRepoListUseCase(prodRepoListUseCase: ProdSearchListUseCase): RepoListUseCase
}