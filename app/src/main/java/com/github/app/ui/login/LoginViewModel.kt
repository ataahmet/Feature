package com.github.app.ui.login

import android.content.SharedPreferences
import com.github.app.reduce.Reducer
import com.github.app.ui.base.BaseViewModel
import com.github.app.util.Keys
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : BaseViewModel<LoginAction, LoginState>() {

    override fun bind() {
        val loginInitialState = LoginState(
            rememberMe = sharedPreferences.getBoolean(Keys.REMEMBER_ME, false)
        )

        val loginReducer: Reducer<LoginState, LoginChange> = { currentState, change ->
            when (change) {
                is LoginChange.Loading -> currentState.copy(
                    isLoading = true,
                    isIdle = false,
                    isLoginError = false,
                    isLoginSuccess = false
                )
                is LoginChange.LoginSuccess -> currentState.copy(
                    isLoading = false,
                    isLoginSuccess = true,
                    isLoginError = false
                )
                is LoginChange.LoginError -> currentState.copy(
                    isLoading = false,
                    isLoginError = true,
                    isLoginSuccess = false,
                    errorMessage = change.message
                )
                is LoginChange.RememberMeToggled -> currentState.copy(
                    rememberMe = change.checked
                )
                is LoginChange.Initialized -> currentState.copy(
                    rememberMe = change.rememberMe
                )
            }
        }

        val initRequest = actions.ofType<LoginAction.Init>().map {
            LoginChange.Initialized(sharedPreferences.getBoolean(Keys.REMEMBER_ME, false))
        }

        val loginRequest = actions.ofType<LoginAction.Login>().switchMap { action ->
            Observable.fromCallable {
                if (action.username.isNotBlank() && action.password.isNotBlank()) {
                    val token = "${action.username}_token"
                    if (action.rememberMe) {
                        sharedPreferences.edit()
                            .putBoolean(Keys.REMEMBER_ME, true)
                            .putString(Keys.AUTH_TOKEN, token)
                            .apply()
                    } else {
                        sharedPreferences.edit()
                            .putBoolean(Keys.REMEMBER_ME, false)
                            .remove(Keys.AUTH_TOKEN)
                            .apply()
                    }
                    LoginChange.LoginSuccess(token) as LoginChange
                } else {
                    LoginChange.LoginError("Kullanıcı adı ve şifre boş olamaz")
                }
            }
                .subscribeOn(Schedulers.io())
                .onErrorReturn { LoginChange.LoginError(it.message ?: "Bilinmeyen hata") }
                .startWith(LoginChange.Loading)
        }

        val rememberMeToggle = actions.ofType<LoginAction.ToggleRememberMe>().map {
            LoginChange.RememberMeToggled(it.checked)
        }

        val allChanges = Observable.merge(listOf(initRequest, loginRequest, rememberMeToggle))

        disposables += allChanges
            .scan(loginInitialState, loginReducer)
            .distinctUntilChanged()
            .subscribe(state::postValue, Timber::e)
    }
}
