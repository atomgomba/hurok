package com.ekezet.hurok

inline fun <TState : Any, TModel : Any, TArgs, TDependency, TAction : Action<TModel, TDependency>, reified TLoop : Loop<TState, TModel, TArgs, TDependency, TAction>> renderState(
    constructor: (model: TModel, args: TArgs?) -> TLoop, model: TModel, args: TArgs? = null,
): TState = constructor(model, args).firstState
