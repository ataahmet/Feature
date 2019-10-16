package com.github.app.app.initializer

import android.app.Application
import io.paperdb.Paper
import javax.inject.Inject

class PaperInitializer @Inject constructor() : AppInitializer {
    override fun init(application: Application) {
        Paper.init(application)
    }
}