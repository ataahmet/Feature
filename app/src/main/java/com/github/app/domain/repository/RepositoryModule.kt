package com.github.app.domain.repository

import com.github.app.data.repository.ProdNavigationRepository
import com.github.app.data.repository.ProdSearchRepository
import com.github.app.data.repository.ProdUserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
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