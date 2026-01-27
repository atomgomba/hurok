package com.ekezet.hurok

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

actual object DispatcherProvider {
    actual val Main: CoroutineDispatcher
        get() = Dispatchers.Main
    actual val Default: CoroutineDispatcher
        get() = Dispatchers.Default
    actual val IO: CoroutineDispatcher
        get() = Dispatchers.IO
}
