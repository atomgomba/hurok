package com.ekezet.hurok.utils

import com.ekezet.hurok.Action
import com.ekezet.hurok.ArgsApplyer
import com.ekezet.hurok.Loop
import com.ekezet.hurok.Renderer

/**
 * Render a given [com.ekezet.hurok.Loop] into a state.
 *
 * @param constructor a [com.ekezet.hurok.Loop] constructor reference
 * @param model model used for rendering
 * @param renderer [com.ekezet.hurok.Renderer] instance used for rendering
 * @param args optional arguments for the [com.ekezet.hurok.Loop]
 * @return a state based on the input arguments
 */
inline fun <TState : Any, TModel : Any, TArgs, TDependency, TAction : Action<TModel, TDependency>, TRenderer : Renderer<TState, TModel>, TLoop : Loop<TState, TModel, TArgs, TDependency, TAction>> renderState(
    constructor: (model: TModel, renderer: TRenderer, args: TArgs?) -> TLoop,
    model: TModel,
    renderer: TRenderer,
    args: TArgs? = null,
): TState = constructor(model, renderer, args).latestState

/**
 * Render a given [com.ekezet.hurok.Loop] into a state.
 *
 * @param constructor a [com.ekezet.hurok.Loop] constructor reference
 * @param model model used for rendering
 * @param renderer [com.ekezet.hurok.Renderer] instance used for rendering
 * @param args optional arguments for the [com.ekezet.hurok.Loop]
 * @param argsApplyer optional args applyer (required if `args` is set)
 * @return a state based on the input arguments
 */
inline fun <TState : Any, TModel : Any, TArgs, TArgsApplyer : ArgsApplyer<TModel, TArgs>, TDependency, TAction : Action<TModel, TDependency>, TRenderer : Renderer<TState, TModel>, TLoop : Loop<TState, TModel, TArgs, TDependency, TAction>> renderState(
    constructor: (model: TModel, renderer: TRenderer, args: TArgs?, argsApplyer: TArgsApplyer?) -> TLoop,
    model: TModel,
    renderer: TRenderer,
    args: TArgs? = null,
    argsApplyer: TArgsApplyer? = null,
): TState = constructor(model, renderer, args, argsApplyer).latestState
