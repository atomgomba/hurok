package com.ekezet.hurok

abstract class ViewState<TModel : Any, TDependency> {
    internal var emitter: ActionEmitter<TModel, TDependency>? = null

    fun emit(action: Action<TModel, TDependency>) =
        emitter?.emit(action)
}
