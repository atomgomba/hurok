package com.ekezet.hurok

/**
 * Action is used to mutate the model and trigger effects.
 */
fun interface Action<TModel : Any, TDependency> {
    /**
     * Result of an action.
     */
    data class Next<TModel : Any, TDependency>(
        val model: TModel? = null,
        val effects: Set<Effect<TModel, TDependency>> = emptySet(),
    )

    val skip: Next<TModel, TDependency>
        get() = Next()

    fun TModel.proceed(): Next<TModel, TDependency>

    fun outcome(
        model: TModel? = null,
        vararg effects: Effect<TModel, TDependency>,
    ): Next<TModel, TDependency> =
        Next(
            model = model,
            effects = effects.toSet(),
        )

    fun mutate(model: TModel? = null): Next<TModel, TDependency> =
        Next(model = model)

    fun trigger(vararg effects: Effect<TModel, TDependency>): Next<TModel, TDependency> =
        Next(effects = effects.toSet())
}
