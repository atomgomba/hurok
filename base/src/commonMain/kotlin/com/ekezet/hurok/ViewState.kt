package com.ekezet.hurok

/**
 * Used to delegate the emitter to the rendered state for starting actions.
 *
 * @param TModel the type of the [Loop] model
 * @param TDependency the type of the [Loop]'s dependency
 */
abstract class ViewState<TModel : Any, TDependency> {
    internal var emitter: ActionEmitter<TModel, TDependency>? = null

    /**
     * Delegate call to [ActionEmitter.emit].
     */
    fun emit(action: Action<TModel, TDependency>) =
        emitter?.emit(action)
}
