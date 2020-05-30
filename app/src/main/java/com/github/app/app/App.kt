package com.github.app.app

import com.github.app.app.initializer.AppInitializer
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import javax.inject.Inject

class App : DaggerApplication() {
    @Inject
    lateinit var appInitializer: Set<@JvmSuppressWildcards AppInitializer>

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        appInitializer.forEach { it.init(this) }
    }
}