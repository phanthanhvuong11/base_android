package com.android.appname.arch.extensions

import android.view.View
import com.android.appname.arch.util.DebounceOnClickListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

fun View.clicks(throttleTime: Long = 400): Flow<Unit> = callbackFlow {
    this@clicks.setOnClickListener {
        offer(Unit)
    }
    awaitClose { this@clicks.setOnClickListener(null) }
}.throttleFirst(throttleTime)

fun View.onClick(interval: Long = 400L, listenerBlock: (View) -> Unit) =
    setOnClickListener(DebounceOnClickListener(interval, listenerBlock))
