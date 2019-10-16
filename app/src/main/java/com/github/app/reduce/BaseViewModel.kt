package com.github.app.reduce

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

abstract class BaseViewModel<A : BaseAction, S : BaseState> : ViewModel() {
    protected val actions: PublishSubject<A> = PublishSubject.create<A>()

    protected val disposables: CompositeDisposable = CompositeDisposable()

    protected abstract val initialState: S

    protected val state = MutableLiveData<S>()

    private val tag by lazy { javaClass.simpleName }

    val observableState: LiveData<S> = MediatorLiveData<S>().apply {
        addSource(state) { data ->
            setValue(data)
        }
    }

    fun dispatch(action: A) {
        actions.onNext(action)
    }

    override fun onCleared() {
        disposables.clear()
    }
}