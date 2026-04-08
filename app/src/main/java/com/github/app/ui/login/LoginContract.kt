package com.github.app.ui.login

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val token: String) : LoginState()
    data class Error(val message: String) : LoginState()
}

sealed class LoginEvent {
    data class OnEmailChanged(val email: String) : LoginEvent()
    data class OnPasswordChanged(val password: String) : LoginEvent()
    object OnLoginClicked : LoginEvent()
}

sealed class LoginEffect {
    data class NavigateToHome(val token: String) : LoginEffect()
    data class ShowError(val message: String) : LoginEffect()
}
