package com.ekezet.hurok.compose

import com.ekezet.hurok.Action

fun <TState : Any, TModel : Any, TArgs, TDependency, TAction : Action<TModel, TDependency>> LoopStateCollectors.WithLifecycle() =
    LoopStateCollector<TState, TModel, TArgs, TDependency, TAction> { it.collectAsStateWithLifecycle() }
