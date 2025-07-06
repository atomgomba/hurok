package com.ekezet.hurok.compose

import androidx.lifecycle.ViewModel
import com.ekezet.hurok.Action
import com.ekezet.hurok.Args
import com.ekezet.hurok.Loop
import com.ekezet.hurok.ViewState
import com.ekezet.hurok.test.CoverageIgnore

/**
 * Common ViewModel for storing a [Loop].
 *
 * See [Common ViewModel](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-viewmodel.html).
 *
 * @property loop the Loop to store
 * @property args the last applied Loop arguments
 */
@CoverageIgnore
data class LoopViewModel<TState : ViewState<TModel, TDependency>, TModel : Any, TArgs : Args<TModel>, TDependency, TAction : Action<TModel, TDependency>>(
    val loop: Loop<TState, TModel, TArgs, TDependency, TAction>,
    var args: TArgs?,
) : ViewModel()
