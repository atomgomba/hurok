package com.ekezet.hurok

import kotlinx.coroutines.CoroutineScope

/**
 * Interface for [Action] emitters.
 *
 * @param TModel the type of the [Loop] model
 * @param TDependency the type of the [Loop]'s dependency
 */
interface ActionEmitter<TModel : Any, TDependency> {
    /**
     * Scope used for emission.
     */
    val scope: CoroutineScope

    /**
     * Emit an action for the [Loop] using the given [scope].
     */
    fun emit(action: Action<TModel, TDependency>)

    /**
     * Add a child emitter on the dependency.
     */
    fun <TChildModel : Any, TChildDependency> addChildEmitter(
        child: ActionEmitter<TChildModel, TChildDependency>,
    )
}

/**
 * Type that represents any [ActionEmitter].
 */
typealias AnyActionEmitter = ActionEmitter<*, *>
