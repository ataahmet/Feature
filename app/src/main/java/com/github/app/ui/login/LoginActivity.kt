package com.github.app.ui.login

import android.os.Bundle
import androidx.activity.viewModels
import com.github.app.R
import com.github.app.databinding.ActivityLoginBinding
import com.github.app.ui.base.BaseViewActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseViewActivity<LoginViewModel, ActivityLoginBinding>() {

    private val generateVM: LoginViewModel by viewModels()

    override val layoutRes = R.layout.activity_login

    override fun provideViewModel() = generateVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.loginContainer, LoginFragment())
                .commit()
        }
    }
}
