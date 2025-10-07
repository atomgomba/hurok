package com.ekezet.hurok.utils

import com.ekezet.hurok.test.CoverageIgnore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@CoverageIgnore
actual object DispatcherProvider {
    actual val Main: CoroutineDispatcher
        get() = Dispatchers.Main
    actual val Default: CoroutineDispatcher
        get() = Dispatchers.Default
    actual val IO: CoroutineDispatcher
        get() = Dispatchers.IO
}
