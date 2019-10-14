package com.github.app.reduce

typealias Reducer<S, C> = (state: S, change: C) -> S
