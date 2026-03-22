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
        bindView()
        observeState()
        viewModel.dispatch(LoginAction.Init)
    }

    private fun bindView() = binding.apply {
        cbRememberMe.setOnCheckedChangeListener { _, isChecked ->
            viewModel.dispatch(LoginAction.ToggleRememberMe(isChecked))
        }

        btnLogin.setOnClickListener {
            val username = etUsername.text?.toString().orEmpty()
            val password = etPassword.text?.toString().orEmpty()
            viewModel.dispatch(LoginAction.Login(username, password, cbRememberMe.isChecked))
        }
    }

    private fun observeState() {
        viewModel.observableState.observe(viewLifecycleOwner, Observer { state ->
            with(binding) {
                when {
                    state.isLoginSuccess -> {
                        activity?.let {
                            it.start<MainActivity>()
                            it.finish()
                        }
                    }
                    state.isLoginError -> {
                        tvError.visibility = View.VISIBLE
                        tvError.text = state.errorMessage
                    }
                    state.isLoading -> {
                        tvError.visibility = View.GONE
                    }
                }
                if (cbRememberMe.isChecked != state.rememberMe) {
                    cbRememberMe.isChecked = state.rememberMe
                }
            }
        })
    }
}
