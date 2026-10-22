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
