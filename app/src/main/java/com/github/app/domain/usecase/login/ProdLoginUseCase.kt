package com.github.app.domain.usecase.login

import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ProdLoginUseCase
    @Inject
    constructor() : LoginUseCase {
        override fun login(
            email: String,
            password: String,
        ): Single<String> {
            // TODO: Gerçek API entegrasyonu yapılacak
            return Single.timer(1, TimeUnit.SECONDS)
                .map { "mock_token_${System.currentTimeMillis()}" }
        }
    }
