package com.github.app.ui.main.splash

import android.os.Bundle
import androidx.activity.viewModels
import com.github.app.R
import com.github.app.databinding.SplashActivityBinding
import com.github.app.extension.listener
import com.github.app.ui.base.BaseViewActivity
import com.github.app.ui.main.MainActivity
import com.github.app.ui.main.repodetail.RepoDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import splitties.activities.start

@AndroidEntryPoint
class SplashActivity : BaseViewActivity<SplashViewModel, SplashActivityBinding>() {

    private val generateVM: SplashViewModel by viewModels()

    override val layoutRes = R.layout.splash_activity

    override fun provideViewModel() = generateVM

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