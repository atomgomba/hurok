package com.ekezet.hurok

/**
 * Render a given [Loop] into a state.
 *
 * @param constructor a [Loop] constructor reference
 * @param model model used for rendering
 * @param renderer [Renderer] instance used for rendering
 * @param args optional arguments for the [Loop]
 * @return a state based on the input arguments
 */
inline fun <TState : ViewState<TModel, TDependency>, TModel : Any, TArgs, TDependency, TAction : Action<TModel, TDependency>, TRenderer : Renderer<TModel, TState>, TLoop : Loop<TState, TModel, TArgs, TDependency, TAction>> renderState(
    constructor: (model: TModel, renderer: TRenderer, args: TArgs?) -> TLoop,
    model: TModel,
    renderer: TRenderer,
    args: TArgs? = null,
): TState = constructor(model, renderer, args).firstState
