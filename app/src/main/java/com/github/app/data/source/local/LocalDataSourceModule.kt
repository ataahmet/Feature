package com.github.app.data.source.local

import com.github.app.data.source.local.user.ProdUserLocalDataSource
import com.github.app.data.source.local.user.UserLocalDataSource
import com.github.app.data.source.navigation.NavigationDataSource
import com.github.app.data.source.navigation.ProdNavigationDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class LocalDataSourceModule {
    @Singleton
    @Binds
    abstract fun bindUserLocalDataSource(prodUserLocalDataSource: ProdUserLocalDataSource): UserLocalDataSource


    @Singleton
    @Binds
    abstract fun bindNavigationDataSource(prodNavigationDataSource: ProdNavigationDataSource): NavigationDataSource
}