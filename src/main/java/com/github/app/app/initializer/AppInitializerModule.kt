package com.github.app.app.initializer

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet

@Module
abstract class AppInitializerModule {
    @Binds
    @IntoSet
    abstract fun bindAndroidThreeTenInitializer(androidThreeTenInitializer: AndroidThreeTenInitializer): AppInitializer

    @Binds
    @IntoSet
    abstract fun bindStethoInitializer(stethoInitializer: StethoInitializer): AppInitializer

    @Binds
    @IntoSet
    abstract fun bindPaperInitializer(paperInitializer: PaperInitializer): AppInitializer
}