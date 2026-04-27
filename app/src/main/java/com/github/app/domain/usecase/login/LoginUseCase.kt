package com.github.app.domain.usecase.login

import com.github.app.domain.usecase.UseCase
import io.reactivex.Single

interface LoginUseCase : UseCase {
    fun login(
        email: String,
        password: String,
    ): Single<String>
}
