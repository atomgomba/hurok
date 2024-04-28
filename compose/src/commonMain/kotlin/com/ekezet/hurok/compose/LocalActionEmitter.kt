package com.ekezet.hurok.compose

import androidx.compose.runtime.compositionLocalOf
import com.ekezet.hurok.AnyActionEmitter

val LocalActionEmitter = compositionLocalOf<AnyActionEmitter?> { null }
