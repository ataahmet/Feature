package com.github.app.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.app.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.loginContainer, LoginFragment())
                .commit()
        }
    }
}
