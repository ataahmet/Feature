package com.github.app.ui.main.repodetail

import androidx.lifecycle.ViewModel
import com.github.app.ui.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module(
    includes = []
)
abstract class RepoDetailActivityModule {
    @ContributesAndroidInjector
    abstract fun bindRepoDetailActivity(): RepoDetailActivity

    @Binds
    @IntoMap
    @ViewModelKey(RepoDetailViewModel::class)
    abstract fun bindRepoDetailViewModel(repoDetailViewModel: RepoDetailViewModel): ViewModel
}
