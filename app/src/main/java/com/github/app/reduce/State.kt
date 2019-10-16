package com.github.app.reduce

data class State(
    val isIdle: Boolean = false,
    val isLoading: Boolean = false,
    val isLoadError: Boolean = false,
    val isLoaded: Boolean = false,
    val data: Any = ""
) : BaseState