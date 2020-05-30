package com.github.app.ui.main.splash

import androidx.lifecycle.ViewModel
import com.github.app.ui.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module(
    includes = []
)
abstract class SplashActivityModule {
    @ContributesAndroidInjector
    abstract fun bindSplashActivity(): SplashActivity

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindSplashViewModel(SplashViewModel: SplashViewModel): ViewModel
}