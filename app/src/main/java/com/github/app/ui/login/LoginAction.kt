package com.github.app.ui.login

import com.github.app.reduce.BaseAction

sealed class LoginAction : BaseAction {
    data class Login(val username: String, val password: String) : LoginAction()
    data class ToggleRememberMe(val isChecked: Boolean) : LoginAction()
}
