package com.github.app.ui.base

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

abstract class BaseActivity<M : BaseViewModel<*, *>> : DaggerAppCompatActivity() {

    protected abstract val viewModelClass: Class<M>

    @Inject
    protected lateinit var viewModelFactory: ViewModelProvider.Factory

    protected lateinit var viewModel: M

    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(viewModelClass)
    }

    protected fun addDisposable(vararg disposables: Disposable) {
        disposables.forEach { this.disposables.add(it) }
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }
}