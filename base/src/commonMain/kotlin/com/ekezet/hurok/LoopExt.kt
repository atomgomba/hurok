package com.ekezet.hurok

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

/**
 * The first state of this [Loop] in a blocking fashion. Useful for testing.
 */
val <TState : ViewState<TModel, TDependency>, TModel : Any, TArgs : Args<TModel>, TDependency, TAction : Action<TModel, TDependency>> Loop<TState, TModel, TArgs, TDependency, TAction>.firstState: TState
    get() = runBlocking { state.first() }
