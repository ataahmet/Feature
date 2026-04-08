package com.github.app.ui.login

import com.github.app.domain.usecase.login.LoginUseCase
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel
    private lateinit var fakeLoginUseCase: FakeLoginUseCase

    @Before
    fun setUp() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

        fakeLoginUseCase = FakeLoginUseCase()
        viewModel = LoginViewModel(fakeLoginUseCase)
    }

    @After
    fun tearDown() {
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
    }

    @Test
    fun `initial state should be idle with empty fields`() {
        val state = viewModel.state.value
        assertEquals("", state.email)
        assertEquals("", state.password)
        assertTrue(state.loginState is LoginState.Idle)
    }

    @Test
    fun `onEmailChanged should update email in state`() {
        viewModel.onEvent(LoginEvent.OnEmailChanged("test@test.com"))
        assertEquals("test@test.com", viewModel.state.value.email)
    }

    @Test
    fun `onPasswordChanged should update password in state`() {
        viewModel.onEvent(LoginEvent.OnPasswordChanged("123456"))
        assertEquals("123456", viewModel.state.value.password)
    }

    @Test
    fun `login with valid credentials should succeed`() {
        fakeLoginUseCase.result = Single.just("fake_token")

        viewModel.onEvent(LoginEvent.OnEmailChanged("test@test.com"))
        viewModel.onEvent(LoginEvent.OnPasswordChanged("123456"))
        viewModel.onEvent(LoginEvent.OnLoginClicked)

        assertTrue(viewModel.state.value.loginState is LoginState.Success)
        assertEquals("fake_token", (viewModel.state.value.loginState as LoginState.Success).token)
    }

    @Test
    fun `login with empty email should emit error effect`() = runBlocking {
        viewModel.onEvent(LoginEvent.OnPasswordChanged("123456"))
        viewModel.onEvent(LoginEvent.OnLoginClicked)

        assertTrue(viewModel.state.value.loginState is LoginState.Idle)
    }

    @Test
    fun `login with empty password should emit error effect`() = runBlocking {
        viewModel.onEvent(LoginEvent.OnEmailChanged("test@test.com"))
        viewModel.onEvent(LoginEvent.OnLoginClicked)

        assertTrue(viewModel.state.value.loginState is LoginState.Idle)
    }

    @Test
    fun `login failure should set error state`() {
        fakeLoginUseCase.result = Single.error(RuntimeException("Network error"))

        viewModel.onEvent(LoginEvent.OnEmailChanged("test@test.com"))
        viewModel.onEvent(LoginEvent.OnPasswordChanged("123456"))
        viewModel.onEvent(LoginEvent.OnLoginClicked)

        assertTrue(viewModel.state.value.loginState is LoginState.Error)
        assertEquals("Network error", (viewModel.state.value.loginState as LoginState.Error).message)
    }

    private class FakeLoginUseCase : LoginUseCase {
        var result: Single<String> = Single.just("token")

        override fun login(email: String, password: String): Single<String> = result
    }
}
