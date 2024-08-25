package com.ekezet.hurok

/**
 * ViewState is used to delegate the emitter to the rendered state for commiting actions.
 */
abstract class ViewState<TModel : Any, TDependency> {
    internal var emitter: ActionEmitter<TModel, TDependency>? = null

    fun emit(action: Action<TModel, TDependency>) =
        emitter?.emit(action)
}
