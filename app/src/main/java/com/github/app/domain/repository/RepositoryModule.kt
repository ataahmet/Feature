package com.github.app.domain.repository

import com.github.app.data.repository.ProdNavigationRepository
import com.github.app.data.repository.ProdSearchRepository
import com.github.app.data.repository.ProdUserRepository
import com.github.app.data.source.local.LocalDataSourceModule
import com.github.app.data.source.remote.api.ApiModule
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module(includes = [ApiModule::class, LocalDataSourceModule::class])
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindSearchRepository(prodSearchRepository: ProdSearchRepository): SearchRepository


    @Singleton
    @Binds
    abstract fun bindUserRepository(prodUserRepository: ProdUserRepository): UserRepository

    @Singleton
    @Binds
    abstract fun bindNavigationRepository(navigationRepository: ProdNavigationRepository): NavigationRepository
}