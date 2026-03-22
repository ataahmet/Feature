package com.github.app.ui.login

sealed class LoginChange {
    object Loading : LoginChange()
    data class LoginSuccess(val token: String) : LoginChange()
    data class LoginError(val message: String) : LoginChange()
    data class RememberMeToggled(val checked: Boolean) : LoginChange()
    data class Initialized(val rememberMe: Boolean) : LoginChange()
}
