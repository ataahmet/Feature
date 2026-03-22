package com.github.app.ui.main.splash

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.viewModels
import com.github.app.R
import com.github.app.databinding.SplashActivityBinding
import com.github.app.extension.listener
import com.github.app.ui.base.BaseViewActivity
import com.github.app.ui.login.LoginActivity
import com.github.app.ui.main.MainActivity
import com.github.app.util.Keys
import dagger.hilt.android.AndroidEntryPoint
import splitties.activities.start
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : BaseViewActivity<SplashViewModel, SplashActivityBinding>() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val generateVM: SplashViewModel by viewModels()

    override val layoutRes = R.layout.splash_activity

    override fun provideViewModel() = generateVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindView()
    }

    private fun bindView() = binding.apply {
        this.motionLayout.listener {
            navigateAfterSplash()
        }
    }

    private fun navigateAfterSplash() {
        val token = sharedPreferences.getString(Keys.AUTH_TOKEN, null)
        val rememberMe = sharedPreferences.getBoolean(Keys.REMEMBER_ME, false)
        if (rememberMe && !token.isNullOrEmpty()) {
            start<MainActivity>()
        } else {
            start<LoginActivity>()
        }
        finish()
    }
}
