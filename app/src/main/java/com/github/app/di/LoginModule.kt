package com.github.app.di

import com.github.app.domain.usecase.login.LoginUseCase
import com.github.app.domain.usecase.login.ProdLoginUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class LoginModule {
    @Binds
    abstract fun bindLoginUseCase(prodLoginUseCase: ProdLoginUseCase): LoginUseCase
}
