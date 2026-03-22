package com.github.app.ui.auth.signin

import com.github.app.reduce.BaseState

data class LoginState(
    val isIdle: Boolean = true,
    val isLoading: Boolean = false,
    val isLoginSuccess: Boolean = false,
    val isLoginError: Boolean = false,
    val rememberMe: Boolean = false,
    val errorMessage: String = ""
) : BaseState
