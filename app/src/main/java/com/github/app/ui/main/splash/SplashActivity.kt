package com.github.app.ui.main.splash

import android.os.Bundle
import com.github.app.R
import com.github.app.databinding.SplashActivityBinding
import com.github.app.extension.listener
import com.github.app.ui.base.BaseViewActivity
import com.github.app.ui.main.MainActivity
import splitties.activities.start

class SplashActivity : BaseViewActivity<SplashViewModel, SplashActivityBinding>() {
    override val layoutRes = R.layout.splash_activity
    override val viewModelClass = SplashViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindView()
    }

    private fun bindView() = binding.apply {
        this.motionLayout.listener {
            gotoMainActivity()
        }
    }

    private fun gotoMainActivity() = this.start<MainActivity>()

}