@file:CoverageIgnore

package com.ekezet.hurok

import com.ekezet.hurok.test.CoverageIgnore
import com.ekezet.hurok.utils.Dispatchers
import kotlin.coroutines.CoroutineContext

actual val DefaultEffectContext: CoroutineContext = Dispatchers.IO
