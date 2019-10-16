package com.github.app.extension

import androidx.lifecycle.MutableLiveData

fun MutableLiveData<Void>.call() {
    value = null
}