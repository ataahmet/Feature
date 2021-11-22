package com.github.app.app.initializer

import com.github.app.app.AndroidThreeTen
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class AndroidThreeTenInitializerModule {
    @AndroidThreeTen
    @Binds
    abstract fun bindAndroidThreeTenInitializer(androidThreeTenInitializer: AndroidThreeTenInitializer): AppInitializer
}