@file:CoverageIgnore

package com.ekezet.hurok.compose

import androidx.compose.runtime.compositionLocalOf
import com.ekezet.hurok.AnyActionEmitter
import com.ekezet.hurok.test.CoverageIgnore

/**
 * Locally scoped [ActionEmitter](com.ekezet.hurok.ActionEmitter) in the composition.
 */
val LocalActionEmitter = compositionLocalOf<AnyActionEmitter?> { null }
