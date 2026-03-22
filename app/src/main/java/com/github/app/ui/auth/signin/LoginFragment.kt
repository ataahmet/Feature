package com.github.app.ui.auth.signin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.github.app.R
import com.github.app.databinding.SignInFragmentBinding
import com.github.app.ui.base.BaseFragment
import com.github.app.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import splitties.activities.start

@AndroidEntryPoint
class LoginFragment : BaseFragment<LoginViewModel, SignInFragmentBinding>() {

    private val generateVM: LoginViewModel by viewModels()

    override val layoutRes = R.layout.sign_in_fragment

    override fun provideViewModel() = generateVM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeState()
    }

    private fun initView() {
        binding.loginButton.setOnClickListener {
            viewModel.dispatch(
                LoginAction.Login(
                    username = binding.usernameEt.text?.toString().orEmpty(),
                    password = binding.passwordEt.text?.toString().orEmpty(),
                    rememberMe = binding.rememberMeCb.isChecked
                )
            )
        }
    }

    private fun observeState() {
        viewModel.observableState.observe(viewLifecycleOwner) { loginState ->
            when {
                loginState.isLoginSuccess -> gotoMainActivity()
                loginState.isLoginError -> showError(loginState.errorMessage)
            }
        }
    }

    private fun gotoMainActivity() {
        requireActivity().start<MainActivity>()
        requireActivity().finish()
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
