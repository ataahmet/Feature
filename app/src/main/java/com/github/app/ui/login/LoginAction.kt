package com.github.app.ui.login

import com.github.app.reduce.BaseAction

sealed class LoginAction : BaseAction {
    object Init : LoginAction()
    data class Login(val username: String, val password: String, val rememberMe: Boolean) : LoginAction()
    data class ToggleRememberMe(val checked: Boolean) : LoginAction()
}
