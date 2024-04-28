package com.ekezet.hurok

inline fun <TState : ViewState<TModel, TDependency>, TModel : Any, TArgs, TDependency, TAction : Action<TModel, TDependency>, TRenderer: Renderer<TModel, TDependency, TState>, TLoop : Loop<TState, TModel, TArgs, TDependency, TAction>> renderState(
    constructor: (model: TModel, renderer: TRenderer, args: TArgs?) -> TLoop,
    model: TModel,
    renderer: TRenderer,
    args: TArgs? = null,
): TState = constructor(model, renderer, args).firstState
