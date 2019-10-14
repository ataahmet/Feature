package com.github.app.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.app.reduce.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

abstract class BaseViewModel<A : BaseAction, S : BaseState> : ViewModel() {
    protected val actions: PublishSubject<A> = PublishSubject.create<A>()


    protected  val initialState: State = State(isIdle = true)

    protected abstract fun  bind()

    protected val state = MutableLiveData<S>()

    private val tag by lazy { javaClass.simpleName }

    protected val disposables: CompositeDisposable = CompositeDisposable()

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