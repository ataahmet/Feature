package com.github.app.app.initializer

import com.github.app.app.Stetho
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class StethoInitializerModule {
    @Stetho
    @Binds
    abstract fun bindStethoInitializer(stethoInitializer: StethoInitializer): AppInitializer
}