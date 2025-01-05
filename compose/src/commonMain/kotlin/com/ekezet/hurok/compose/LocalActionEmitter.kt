@file:CoverageIgnore
package com.ekezet.hurok.compose

import androidx.compose.runtime.compositionLocalOf
import com.ekezet.hurok.AnyActionEmitter
import com.ekezet.hurok.test.CoverageIgnore

val LocalActionEmitter = compositionLocalOf<AnyActionEmitter?> { null }
