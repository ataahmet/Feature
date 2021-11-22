package com.github.app.data.source.local.paper

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.paperdb.Book
import io.paperdb.Paper
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object PaperModule{
    @Singleton
    @Provides
    fun provideDefaultBook(): Book = Paper.book()
}