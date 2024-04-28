package com.ekezet.hurok

fun interface LoopBuilder<out TState : ViewState<TModel, TDependency>, TModel : Any, in TArgs, TDependency, in TAction : Action<TModel, TDependency>> {
    fun build(args: TArgs?): Loop<TState, TModel, TArgs, TDependency, TAction>
}
