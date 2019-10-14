package com.github.app.reduce

interface BaseAction {
    fun obfuscatedString() = "${javaClass.simpleName}@${hashCode()}"
}