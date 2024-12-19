package com.ekezet.hurok

import com.ekezet.hurok.Action.Next

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

    fun TModel.proceed(): Next<TModel, TDependency>
}

val <TModel : Any, TDependency> Action<TModel, TDependency>.skip: Next<TModel, TDependency>
    get() = Next()

fun <TModel : Any, TDependency> Action<TModel, TDependency>.outcome(
    model: TModel? = null,
    vararg effects: Effect<TModel, TDependency>,
): Next<TModel, TDependency> =
    Next(
        model = model,
        effects = effects.toSet(),
    )

fun <TModel : Any, TDependency> Action<TModel, TDependency>.mutate(model: TModel? = null): Next<TModel, TDependency> =
    Next(model = model)

fun <TModel : Any, TDependency> Action<TModel, TDependency>.trigger(vararg effects: Effect<TModel, TDependency>): Next<TModel, TDependency> =
    Next(effects = effects.toSet())
