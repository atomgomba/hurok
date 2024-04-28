package com.ekezet.hurok

/**
 * Effect is used to perform background tasks.
 */
fun interface Effect<TModel : Any, TDependency> {
    suspend fun ActionEmitter<TModel, TDependency>.trigger(dependency: TDependency?): Any?
}
