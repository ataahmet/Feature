package com.github.app.data.source.remote.api

import android.app.Application
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.github.app.BuildConfig
import com.serjltt.moshi.adapters.Wrapped
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import io.paperdb.Book
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class ApiModule {

    @Singleton
    @Provides
    fun provideCache(application: Application) =
        Cache(File(application.cacheDir, "retrofit-cache"), CACHE_SIZE_IN_BYTES)

    @Singleton
    @Provides
    fun provideRequestInterceptor(defaultBook: Book) = Interceptor { chain ->
        val originalRequest = chain.request()
        val url = originalRequest.url().newBuilder()
            .build()
        val request = originalRequest.newBuilder().url(url).build()
        chain.proceed(request)
    }

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
        else HttpLoggingInterceptor.Level.NONE
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        cache: Cache,
        requestInterceptor: Interceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .cache(cache)
        .addInterceptor(requestInterceptor)
        .addInterceptor(httpLoggingInterceptor)
        .addNetworkInterceptor(StethoInterceptor())
        .connectTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
        .build()

    @Singleton
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(Wrapped.ADAPTER_FACTORY)
        .add(DateAdapter())
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit =
        Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()

    @Singleton
    @Provides
    fun provideTicketWalletdApi(retrofit: Retrofit): GithubApi = retrofit.create(
        GithubApi::class.java
    )

    companion object ApiPaths {
        private const val TIMEOUT_IN_SECONDS: Long = 30
        private const val CACHE_SIZE_IN_BYTES: Long = 10 * 1024 * 1024

        private const val API_HOST = "api.github.com"
        private const val API_BASE_URL = "https://$API_HOST/"
    }
}