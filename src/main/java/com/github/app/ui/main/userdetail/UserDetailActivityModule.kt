package com.github.app.ui.main.userdetail

import androidx.lifecycle.ViewModel
import com.github.app.ui.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module(
    includes = []
)
abstract class UserDetailActivityModule {
    @ContributesAndroidInjector
    abstract fun bindUserDetailActivity(): UserDetailActivity

    @Binds
    @IntoMap
    @ViewModelKey(UserDetailViewModel::class)
    abstract fun bindUserDetailViewModel(userDetailViewModel: UserDetailViewModel): ViewModel
}
