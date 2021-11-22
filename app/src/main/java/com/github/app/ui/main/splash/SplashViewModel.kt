package com.github.app.ui.main.splash

import com.github.app.reduce.Action
import com.github.app.reduce.State
import com.github.app.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() : BaseViewModel<Action, State>() {
    override fun bind() {}
}