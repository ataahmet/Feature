package com.github.app.ui.main.splash

import android.content.SharedPreferences
import com.github.app.reduce.Action
import com.github.app.reduce.State
import com.github.app.ui.base.BaseViewModel
import com.github.app.util.Keys
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : BaseViewModel<Action, State>() {

    fun isRemembered(): Boolean {
        return sharedPreferences.getBoolean(Keys.REMEMBER_ME, false) &&
            !sharedPreferences.getString(Keys.TOKEN, null).isNullOrEmpty()
    }

    override fun bind() {}
}
