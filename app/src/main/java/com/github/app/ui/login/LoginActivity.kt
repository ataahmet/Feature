package com.github.app.ui.login

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.github.app.ui.main.MainActivity
import com.github.app.ui.theme.GithubTheme
import dagger.hilt.android.AndroidEntryPoint
import splitties.activities.start

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GithubTheme {
                LoginScreen(
                    viewModel = loginViewModel,
                    onLoginSuccess = { gotoMainActivity() },
                )
            }
        }
    }

    private fun gotoMainActivity() {
        start<MainActivity>()
        finish()
    }
}
