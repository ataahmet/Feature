package com.github.app.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.app.reduce.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.ReplaySubject

abstract class BaseViewModel<A : BaseAction, S : BaseState> : ViewModel() {

    protected val actions: ReplaySubject<A> = ReplaySubject.create<A>()

    protected val initialState = State(isIdle = true)

    protected val disposables: CompositeDisposable = CompositeDisposable()

    protected abstract fun bind()

    protected val state = MutableLiveData<S>()

    private val tag by lazy { javaClass.simpleName }

    protected val reducer: Reducer<State, Change> = { state, change ->
        when (change) {
            is Change.Loading -> state.copy(
                isLoading = true,
                isIdle = false,
                isLoadError = false,
                isLoaded = false
            )
            is Change.LoadError -> state.copy(
                isLoading = false,
                isLoadError = true,
                isLoaded = false
            )
            is Change.Loaded -> state.copy(
                isLoading = false,
                isLoadError = false,
                isLoaded = true,
                data = change.value
            )

        }
    }

    init {
        this.bind()
    }


    val observableState: LiveData<S> = MediatorLiveData<S>().apply {
        addSource(state) { data ->
            postValue(data)
        }
    }

    fun dispatch(action: A) {
        actions.onNext(action)
    }

    override fun onCleared() {
        disposables.clear()
    }

}