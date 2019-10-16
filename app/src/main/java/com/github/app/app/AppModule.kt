package com.github.app.app

import android.app.Application
import android.content.Context
import com.github.app.app.initializer.AppInitializerModule
import com.github.app.domain.usecase.UseCaseModule
import com.github.app.ui.ActivityModule
import dagger.Binds
import dagger.Module
import javax.inject.Qualifier
import javax.inject.Singleton

@Module(
    includes = [
        AppInitializerModule::class,
        ActivityModule::class,
        UseCaseModule::class
    ]
)
abstract class AppModule {
    @Singleton
    @Binds
    abstract fun bindApplication(application: App): Application

    @Singleton
    @Binds
    @ApplicationContext
    abstract fun bindApplicationContext(application: Application): Context

}

@Qualifier
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationContext