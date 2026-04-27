package com.github.app.ui.login

import androidx.lifecycle.ViewModel
import com.github.app.domain.usecase.login.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val _state = MutableStateFlow(LoginViewState())
    val state: StateFlow<LoginViewState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<LoginEffect>(extraBufferCapacity = 1)
    val effect: SharedFlow<LoginEffect> = _effect.asSharedFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnEmailChanged -> {
                _state.value = _state.value.copy(email = event.email)
            }
            is LoginEvent.OnPasswordChanged -> {
                _state.value = _state.value.copy(password = event.password)
            }
            is LoginEvent.OnLoginClicked -> performLogin()
        }
    }

    private fun performLogin() {
        val currentState = _state.value
        if (!isValidInput(currentState.email, currentState.password)) {
            _effect.tryEmit(LoginEffect.ShowError("E-posta ve parola bos birakilamaz"))
            return
        }

        val disposable = loginUseCase.login(currentState.email, currentState.password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _state.value = _state.value.copy(loginState = LoginState.Loading) }
            .subscribe(
                { token ->
                    _state.value = _state.value.copy(loginState = LoginState.Success(token))
                    _effect.tryEmit(LoginEffect.NavigateToHome(token))
                },
                { error ->
                    val message = error.message ?: "Bilinmeyen hata"
                    _state.value = _state.value.copy(loginState = LoginState.Error(message))
                    _effect.tryEmit(LoginEffect.ShowError(message))
                    Timber.e(error, "Login failed")
                }
            )
        val tmp: String?=null
        tmp!!.length
        disposables.add(disposable)
    }

    private fun isValidInput(email: String, password: String): Boolean {
        return email.isNotBlank() && password.isNotBlank()
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}

data class LoginViewState(
    val email: String = "",
    val password: String = "",
    val loginState: LoginState = LoginState.Idle
)
