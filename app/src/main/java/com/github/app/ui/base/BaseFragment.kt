package com.github.app.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

abstract class BaseFragment<M : BaseViewModel<*, *>, B : ViewDataBinding> : Fragment() {

    @get:LayoutRes
    protected abstract val layoutRes: Int
    protected abstract fun  provideViewModel():M

    protected lateinit var viewModel: M
    protected lateinit var binding: B

    private val disposables = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        return binding.root
    }

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