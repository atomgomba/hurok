package com.ekezet.hurok

/**
 * Interface for building a given [Loop].
 */
fun interface LoopBuilder<out TState : Any, TModel : Any, in TArgs, TDependency, in TAction : Action<TModel, TDependency>> {
    /**
     * @param args input arguments for the [Loop]
     * @return a new [Loop]
     */
    fun build(args: TArgs?): Loop<TState, TModel, TArgs, TDependency, TAction>
}

/**
 * @return a [LoopBuilder] for an existing loop (this)
 */
@Suppress("UNCHECKED_CAST")
fun <TState : Any, TModel : Any, TArgs, TDependency, TAction : Action<TModel, TDependency>, TLoopBuilder : LoopBuilder<TState, TModel, TArgs, TDependency, TAction>> Loop<TState, TModel, TArgs, TDependency, TAction>.asBuilder(): TLoopBuilder =
    (LoopBuilder<TState, TModel, TArgs, TDependency, TAction> { args: TArgs? ->
        this@asBuilder.apply {
            args?.let(::applyArgs)
        }
    }) as TLoopBuilder
