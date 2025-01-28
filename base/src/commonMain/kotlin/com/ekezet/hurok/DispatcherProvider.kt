package com.ekezet.hurok

import com.ekezet.hurok.test.CoverageIgnore
import kotlinx.coroutines.CoroutineDispatcher

/**
 * Multiplatform coroutine dispatcher provider.
 *
 * This must be implemented for each platform.
 */
@CoverageIgnore
expect object DispatcherProvider {
    val Main: CoroutineDispatcher
    val Default: CoroutineDispatcher
    val IO: CoroutineDispatcher
}
