package com.ekezet.hurok.compose

import com.ekezet.hurok.Action
import com.ekezet.hurok.Args
import com.ekezet.hurok.ViewState

fun <TState : ViewState<TModel, TDependency>, TModel : Any, TArgs : Args<TModel>, TDependency, TAction : Action<TModel, TDependency>> LoopStateCollectors.WithLifecycle() =
    LoopStateCollector<TState, TModel, TArgs, TDependency, TAction> { it.collectAsStateWithLifecycle() }
