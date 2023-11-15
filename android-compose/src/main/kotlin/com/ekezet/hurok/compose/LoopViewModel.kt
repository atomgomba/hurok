package com.ekezet.hurok.compose

import androidx.lifecycle.ViewModel
import com.ekezet.hurok.Action
import com.ekezet.hurok.Loop

data class LoopViewModel<TState : Any, TModel : Any, TArgs, TDependency, TAction : Action<TModel, TDependency>>(
    val loop: Loop<TState, TModel, TArgs, TDependency, TAction>,
) : ViewModel()
