package com.github.app.data.source.local.paper

import dagger.Module
import dagger.Provides
import io.paperdb.Book
import io.paperdb.Paper
import javax.inject.Singleton

@Module
class PaperModule {
    @Singleton
    @Provides
    fun provideDefaultBook(): Book = Paper.book()
}