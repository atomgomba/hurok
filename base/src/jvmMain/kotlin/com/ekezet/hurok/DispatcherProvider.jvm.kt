package com.ekezet.hurok

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual object DispatcherProvider {
    actual val Main: CoroutineDispatcher
        get() = Dispatchers.Main
    actual val Default: CoroutineDispatcher
        get() = Dispatchers.Default
    actual val IO: CoroutineDispatcher
        get() = Dispatchers.IO
}
