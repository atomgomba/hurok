package com.ekezet.hurok

import com.ekezet.hurok.test.CoverageIgnore
import kotlinx.coroutines.CoroutineDispatcher

@CoverageIgnore
expect object DispatcherProvider {
    val Main: CoroutineDispatcher
    val Default: CoroutineDispatcher
    val IO: CoroutineDispatcher
}
