package com.github.app.reduce

interface BaseState {
    fun obfuscatedString() = "${javaClass.simpleName}@${hashCode()}"
}