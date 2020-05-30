package com.github.app.extension

import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout

fun View.visible() {
    visibility = VISIBLE
}

fun View.invisible() {
    visibility = INVISIBLE
}

fun View.gone() {
    visibility = GONE
}

fun ViewGroup.getInflater(): LayoutInflater = LayoutInflater.from(context)

fun View.onSingleClick(debounceTime: Long = 2000L, action: () -> Unit) {
    var lastClickTime: Long = 0
    setOnClickListener {
        if (SystemClock.elapsedRealtime() - lastClickTime >= debounceTime) action()
        lastClickTime = SystemClock.elapsedRealtime()
    }
}
fun MotionLayout.listener(invoke : () -> Unit){
    this.setTransitionListener(object :MotionLayout.TransitionListener{
        override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}

        override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}

        override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {}

        override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
            invoke.invoke()
        }

    })
}