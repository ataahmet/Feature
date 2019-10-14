package com.github.app.ui.main

import androidx.lifecycle.ViewModel
import com.github.app.ui.ViewModelKey
import com.github.app.ui.main.home.HomeModule
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module(
    includes = [HomeModule::class]
)
abstract class MainModule {
    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel
}
