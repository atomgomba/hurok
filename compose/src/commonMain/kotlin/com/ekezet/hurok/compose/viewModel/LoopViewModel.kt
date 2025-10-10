package com.ekezet.hurok.compose.viewModel

import androidx.lifecycle.ViewModel
import com.ekezet.hurok.Action
import com.ekezet.hurok.Loop
import com.ekezet.hurok.test.CoverageIgnore

/**
 * Common ViewModel for storing a [Loop].
 *
 * See [Common ViewModel](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-viewmodel.html).
 *
 * @property loop the Loop to store
 */
@CoverageIgnore
data class LoopViewModel<TState : Any, TModel : Any, TArgs, TDependency, TAction : Action<TModel, TDependency>>(
    val loop: Loop<TState, TModel, TArgs, TDependency, TAction>,
) : ViewModel()
