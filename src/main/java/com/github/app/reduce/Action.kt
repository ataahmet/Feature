package com.github.app.reduce

sealed class Action : BaseAction {
    data class ActionUserDetail(val ownerName: String) : Action()
}