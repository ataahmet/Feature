package com.github.app.ui.auth.signin

import com.github.app.reduce.BaseAction

sealed class LoginAction : BaseAction {
    data class Login(
        val username: String,
        val password: String,
        val rememberMe: Boolean
    ) : LoginAction()
}
