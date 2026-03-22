package com.github.app.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.app.R
import com.github.app.ui.auth.signin.LoginFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.login_container, LoginFragment())
                .commit()
        }
    }
}
