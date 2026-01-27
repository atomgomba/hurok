package com.ekezet.hurok

/**
 * Interface for [Action] emitters.
 *
 * @param TModel the type of the [Loop] model
 * @param TDependency the type of the [Loop]'s dependency
 */
fun interface ActionEmitter<TModel : Any, TDependency> {
    /**
     * Emit an action to change the state or trigger an effect.
     */
    fun emit(action: Action<TModel, TDependency>)
}

/**
 * Type that represents any [ActionEmitter].
 */
typealias AnyActionEmitter = ActionEmitter<*, *>
