package com.github.app.ui.auth.signin

import android.content.SharedPreferences
import androidx.core.content.edit
import com.github.app.ui.base.BaseViewModel
import com.github.app.util.Keys
import dagger.hilt.android.lifecycle.HiltViewModel
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
        disposables += actions.ofType<LoginAction.Login>()
            .subscribeOn(Schedulers.computation())
            .map { action ->
                if (action.username.isNotBlank() && action.password.isNotBlank()) {
                    sharedPreferences.edit {
                        putBoolean(Keys.REMEMBER_ME, action.rememberMe)
                        if (action.rememberMe) {
                            putString(Keys.TOKEN, action.username)
                        } else {
                            remove(Keys.TOKEN)
                        }
                    }
                    LoginState(isLoginSuccess = true, rememberMe = action.rememberMe)
                } else {
                    LoginState(isLoginError = true, errorMessage = "Kullanıcı adı ve şifre boş olamaz")
                }
            }
            .subscribe(state::postValue, Timber::e)
    }
}
