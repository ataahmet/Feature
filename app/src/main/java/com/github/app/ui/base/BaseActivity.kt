package com.github.app.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseActivity<M : BaseViewModel<*, *>> : AppCompatActivity() {

    private val disposables = CompositeDisposable()

    protected lateinit var viewModel: M

    abstract fun provideViewModel(): M

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideViewModel()
    }

    protected fun addDisposable(vararg disposables: Disposable) {
        disposables.forEach { this.disposables.add(it) }
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }
}
