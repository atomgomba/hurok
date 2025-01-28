package com.ekezet.hurok

/**
 * Effect is used to perform background tasks.
 *
 * @param TModel the type of the [Loop] model
 * @param TDependency the type of the [Loop]'s dependency
 */
fun interface Effect<TModel : Any, TDependency> {
    /**
     * Execute this [Effect] and optionally emit one or more [Action]s.
     *
     * @param dependency holder for external dependencies for the [Loop]
     */
    suspend fun ActionEmitter<TModel, TDependency>.trigger(dependency: TDependency?): Any?
}
