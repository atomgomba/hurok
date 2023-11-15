package com.ekezet.hurok

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

val <TState : Any, TModel : Any, TArgs, TDependency, TAction : Action<TModel, TDependency>> Loop<TState, TModel, TArgs, TDependency, TAction>.firstState: TState
    get() = runBlocking { state.first() }
