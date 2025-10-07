package com.ekezet.hurok.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import com.ekezet.hurok.Action
import com.ekezet.hurok.Loop

fun interface LoopStateCollector<TState : Any, TModel : Any, TArgs, TDependency, TAction : Action<TModel, TDependency>> {
    @Composable
    operator fun invoke(loop: Loop<TState, TModel, TArgs, TDependency, TAction>): State<TState>
}

object LoopStateCollectors

fun <TState : Any, TModel : Any, TArgs, TDependency, TAction : Action<TModel, TDependency>> LoopStateCollectors.Standard() =
    LoopStateCollector<TState, TModel, TArgs, TDependency, TAction> { it.collectAsState() }
