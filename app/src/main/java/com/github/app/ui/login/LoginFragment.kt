package com.github.app.ui.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.github.app.R
import com.github.app.databinding.FragmentLoginBinding
import com.github.app.ui.base.BaseFragment
import com.github.app.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import splitties.activities.start

@AndroidEntryPoint
class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding>() {

    private val generateVM: LoginViewModel by viewModels()

    override val layoutRes = R.layout.fragment_login

    override fun provideViewModel() = generateVM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBinding()
        observeState()
    }

    private fun setupBinding() {
        binding.apply {
            viewModel = this@LoginFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
            cbRememberMe.isChecked = this@LoginFragment.viewModel.getSavedRememberMe()
            btnLogin.setOnClickListener {
                val username = etUsername.text?.toString().orEmpty()
                val password = etPassword.text?.toString().orEmpty()
                this@LoginFragment.viewModel.dispatch(LoginAction.Login(username, password))
            }
            cbRememberMe.setOnCheckedChangeListener { _, isChecked ->
                this@LoginFragment.viewModel.dispatch(LoginAction.ToggleRememberMe(isChecked))
            }
        }
    }

    private fun observeState() {
        viewModel.observableState.observe(viewLifecycleOwner, Observer { loginState ->
            when {
                loginState.isLoginSuccess -> gotoMainActivity()
                loginState.isLoginError -> Unit
            }
        })
    }

    private fun gotoMainActivity() {
        activity?.start<MainActivity>()
        activity?.finish()
    }
}
