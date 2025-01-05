package com.ekezet.hurok

import com.ekezet.hurok.Action.Next
import com.ekezet.hurok.test.CoverageIgnore

/**
 * Action is used to mutate the model and trigger effects.
 */
@CoverageIgnore
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

@Suppress("UnusedReceiverParameter")
@get:CoverageIgnore
val <TModel : Any, TDependency> Action<TModel, TDependency>.skip: Next<TModel, TDependency>
    get() = Next()

@Suppress("UnusedReceiverParameter")
@CoverageIgnore
fun <TModel : Any, TDependency> Action<TModel, TDependency>.outcome(
    model: TModel? = null,
    vararg effects: Effect<TModel, TDependency>,
): Next<TModel, TDependency> =
    Next(
        model = model,
        effects = effects.toSet(),
    )

@Suppress("UnusedReceiverParameter")
@CoverageIgnore
fun <TModel : Any, TDependency> Action<TModel, TDependency>.mutate(model: TModel? = null): Next<TModel, TDependency> =
    Next(model = model)

@Suppress("UnusedReceiverParameter")
@CoverageIgnore
fun <TModel : Any, TDependency> Action<TModel, TDependency>.trigger(vararg effects: Effect<TModel, TDependency>): Next<TModel, TDependency> =
    Next(effects = effects.toSet())
