package com.github.app.ui.login

import android.content.SharedPreferences
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
        val toggleRememberMe = actions.ofType<LoginAction.ToggleRememberMe>()
            .doOnNext { action ->
                sharedPreferences.edit()
                    .putBoolean(Keys.REMEMBER_ME, action.isChecked)
                    .apply()
            }
            .map { action -> LoginState(rememberMe = action.isChecked) }

        val loginRequest = actions.ofType<LoginAction.Login>()
            .switchMap { action ->
                Observable.just(action)
                    .subscribeOn(Schedulers.io())
                    .map { login ->
                        val token = "token_${login.username}"
                        val rememberMe = sharedPreferences.getBoolean(Keys.REMEMBER_ME, false)
                        if (rememberMe) {
                            sharedPreferences.edit()
                                .putString(Keys.TOKEN, token)
                                .apply()
                        }
                        LoginState(isLoginSuccess = true)
                    }
                    .onErrorReturn { error ->
                        LoginState(isLoginError = true, errorMessage = error.message.orEmpty())
                    }
                    .startWith(LoginState(isLoading = true))
            }

        disposables += Observable.merge(toggleRememberMe, loginRequest)
            .subscribe(state::postValue, Timber::e)
    }

    fun getSavedRememberMe(): Boolean = sharedPreferences.getBoolean(Keys.REMEMBER_ME, false)
}
