package com.github.app.reduce

sealed class Change {
    object Loading : Change()
    data class Loaded(val value: Any) : Change()
    data class LoadError(val throwable: Throwable) : Change()
}
