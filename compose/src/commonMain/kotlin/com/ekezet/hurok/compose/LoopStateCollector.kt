package com.ekezet.hurok.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import com.ekezet.hurok.Action
import com.ekezet.hurok.Loop
import com.ekezet.hurok.ViewState

fun interface LoopStateCollector<TState : ViewState<TModel, TDependency>, TModel : Any, TArgs, TDependency, TAction : Action<TModel, TDependency>> {
    @Composable
    operator fun invoke(loop: Loop<TState, TModel, TArgs, TDependency, TAction>): State<TState>
}

object LoopStateCollectors

fun <TState : ViewState<TModel, TDependency>, TModel : Any, TArgs, TDependency, TAction : Action<TModel, TDependency>> LoopStateCollectors.Standard() =
    LoopStateCollector<TState, TModel, TArgs, TDependency, TAction> { it.collectAsState() }
