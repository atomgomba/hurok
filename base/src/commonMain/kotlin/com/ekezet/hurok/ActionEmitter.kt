package com.ekezet.hurok

import kotlinx.coroutines.CoroutineScope

interface ActionEmitter<TModel : Any, TDependency> {
    val parentScope: CoroutineScope

    /**
     * Emit an action.
     */
    fun emit(action: Action<TModel, TDependency>)

    fun <TChildModel : Any, TChildDependency> addChildEmitter(
        child: ActionEmitter<TChildModel, TChildDependency>,
    )
}

typealias AnyActionEmitter = ActionEmitter<*, *>
