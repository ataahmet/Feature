package com.github.app.ui

import androidx.lifecycle.ViewModelProvider
import com.github.app.ui.main.MainModule
import com.github.app.ui.main.repodetail.RepoDetailActivityModule
import com.github.app.ui.main.userdetail.UserDetailActivityModule
import dagger.Binds
import dagger.Module

@Module(includes = [MainModule::class, UserDetailActivityModule::class, RepoDetailActivityModule::class])
abstract class ActivityModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}