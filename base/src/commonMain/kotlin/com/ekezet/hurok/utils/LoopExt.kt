package com.ekezet.hurok.utils

import com.ekezet.hurok.Action
import com.ekezet.hurok.Loop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

/**
 * The first state of this [com.ekezet.hurok.Loop] in a blocking fashion. Useful for testing.
 */
val <TState : Any, TModel : Any, TArgs : TModel, TDependency, TAction : Action<TModel, TDependency>> Loop<TState, TModel, TArgs, TDependency, TAction>.firstState: TState
    get() = runBlocking { state.first() }
