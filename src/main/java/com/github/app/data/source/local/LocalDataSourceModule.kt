package com.github.app.data.source.local

import com.github.app.data.source.local.paper.PaperModule
import com.github.app.data.source.local.user.ProdUserLocalDataSource
import com.github.app.data.source.local.user.UserLocalDataSource
import com.github.app.data.source.navigation.NavigationDataSource
import com.github.app.data.source.navigation.ProdNavigationDataSource
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module(includes = [PaperModule::class])
abstract class LocalDataSourceModule {
    @Singleton
    @Binds
    abstract fun bindUserLocalDataSource(prodUserLocalDataSource: ProdUserLocalDataSource): UserLocalDataSource


    @Singleton
    @Binds
    abstract fun bindNavigationDataSource(prodNavigationDataSource: ProdNavigationDataSource): NavigationDataSource
}