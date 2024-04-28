package com.ekezet.hurok

import kotlinx.coroutines.CoroutineDispatcher

expect object DispatcherProvider {
    val Main: CoroutineDispatcher
    val Default: CoroutineDispatcher
    val IO: CoroutineDispatcher
}
