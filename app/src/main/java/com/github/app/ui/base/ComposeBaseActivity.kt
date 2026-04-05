package com.github.app.ui.base

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.github.app.ui.theme.GithubTheme

abstract class ComposeBaseActivity<M : BaseViewModel<*, *>> : BaseActivity<M>() {

    @Composable
    abstract fun Content()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GithubTheme {
                Content()
            }
        }
    }
}
