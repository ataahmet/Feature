package com.github.app.app

import android.app.Application
import com.github.app.app.initializer.AppInitializer
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import javax.inject.Qualifier

@HiltAndroidApp
class App : Application() {

    @Stetho
    @Inject
    lateinit var stetho: AppInitializer

    @Paper
    @Inject
    lateinit var paper: AppInitializer

    @AndroidThreeTen
    @Inject
    lateinit var threeTen: AppInitializer

    override fun onCreate() {
        super.onCreate()
        stetho.init(this)
        paper.init(this)
        threeTen.init(this)
    }
}

@Qualifier
annotation class AndroidThreeTen

@Qualifier
annotation class Paper

@Qualifier
annotation class Stetho