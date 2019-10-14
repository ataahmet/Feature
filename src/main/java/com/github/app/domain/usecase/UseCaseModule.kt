package com.github.app.domain.usecase

import com.github.app.domain.repository.RepositoryModule
import com.github.app.domain.usecase.repolist.ProdSearchListUseCase
import com.github.app.domain.usecase.repolist.RepoListUseCase
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module(includes = [RepositoryModule::class])
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