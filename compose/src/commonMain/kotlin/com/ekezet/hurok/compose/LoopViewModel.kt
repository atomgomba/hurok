package com.ekezet.hurok.compose

import androidx.lifecycle.ViewModel
import com.ekezet.hurok.Action
import com.ekezet.hurok.Loop
import com.ekezet.hurok.ViewState

data class LoopViewModel<TState : ViewState<TModel, TDependency>, TModel : Any, TArgs, TDependency, TAction : Action<TModel, TDependency>>(
    val loop: Loop<TState, TModel, TArgs, TDependency, TAction>
) : ViewModel()
