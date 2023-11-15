package com.ekezet.hurok

fun interface LoopBuilder<out TState : Any, TModel : Any, in TArgs, TDependency, in TAction : Action<TModel, TDependency>> {
    fun build(args: TArgs?): Loop<TState, TModel, TArgs, TDependency, TAction>
}
