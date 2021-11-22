package com.github.app.app.initializer

import com.github.app.app.Paper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class PaperInitializerModule {
    @Paper
    @Binds
    abstract fun bindPaperInitializer(paperInitializer: PaperInitializer): AppInitializer
}