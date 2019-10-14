package com.github.app.domain

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.io.IOException

abstract class NetworkBoundResource<RequestType, ResultType> {
    private val result: Observable<Resource<ResultType>>

    init {
        @Suppress("LeakingThis")
        result = when {
            shouldFetch() -> createCall()
                .toObservable()
                // Request API on IO Scheduler
                .subscribeOn(Schedulers.io())
                // Read/Write to disk on Computation Scheduler
                .observeOn(Schedulers.computation())
                .map<Resource<ResultType>> {
                    mapCallResult(it).let {
                        saveCallResult(it)
                        Success(it)
                    }
                }
                .onErrorReturn {
                    when (it) {
                        is HttpException -> when (it.code()) {
                            401 -> UnauthorizedError()
                            422 -> FormError(it.message(), mapOf())
                            in 400..499 -> MessageError(it.message())
                            in 500..599 -> ServerError()
                            else -> UnknownError()
                        }
                        is IOException -> NetworkError()
                        else -> UnknownError()
                    }
                }
                // Read results in Android Main Thread (UI)
                //.observeOn(AndroidSchedulers.mainThread())
                .startWith(Loading())

            else -> loadFromDb()
                .subscribeOn(Schedulers.computation())
                .map<Resource<ResultType>> { Success(it) }
                .onErrorReturn { UnknownError() }
                // Read results in Android Main Thread (UI)
                //.observeOn(AndroidSchedulers.mainThread())
                .startWith(Loading())
                .toObservable()
        }
    }

    protected open fun shouldFetch() = true
    protected open fun loadFromDb(): Flowable<ResultType> = Flowable.empty()
    protected abstract fun createCall(): Single<RequestType>
    protected abstract fun mapCallResult(response: RequestType): ResultType
    protected open fun saveCallResult(result: ResultType) {}
    fun asObservable() = result
}