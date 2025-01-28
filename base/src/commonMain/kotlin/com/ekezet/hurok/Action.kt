package com.ekezet.hurok

import com.ekezet.hurok.Action.Next
import com.ekezet.hurok.test.CoverageIgnore

/**
 * Action is used to mutate the model and/or trigger effects.
 *
 * For example:
 *
 * ```kotlin
 * data object OnDetailsExpandClick : HelpScreenAction {
 *     override fun HelpScreenModel.proceed() =
 *         mutate(copy(showDetails = !showDetails))
 * }
 * ```
 *
 * Or using [SAM](https://kotlinlang.org/docs/fun-interfaces.html):
 *
 * ```kotlin
 * val OnDetailExpandClick: HelpScreenAction
 *     get() = { next(copy(showDetails = !showDetails)) }
 * ```
 *
 * @param TModel the type of the [Loop] model
 * @param TDependency the type of the [Loop]'s dependency
 * @see mutate
 * @see next
 * @see outcome
 * @see skip
 * @see trigger
 */
@CoverageIgnore
fun interface Action<TModel : Any, TDependency> {
    /**
     * Represent the result of an [Action].
     */
    data class Next<TModel : Any, TDependency>(
        /**
         * The updated model or `null` to make no changes.
         */
        val model: TModel? = null,

        /**
         * Any optional effects that may be triggered.
         */
        val effects: Set<Effect<TModel, TDependency>> = emptySet(),
    )

    /**
     * Execute this [Action].
     *
     * @return the result as a [Next] instance
     */
    fun TModel.proceed(): Next<TModel, TDependency>
}

/**
 * Shorthand for returning an action result without any changes or effects triggered.
 *
 * For example:
 *
 * ```kotlin
 * data object OnLoginButtonClick : MainScreenAction {
 *     override fun MainScreenModel.proceed() =
 *         if (username.isBlank() || password.isBlank()) {
 *             skip
 *         } else {
 *             outcome(copy(loading = true), StartLogin(username, password))
 *         }
 * }
 * ```
 */
@Suppress("UnusedReceiverParameter")
@get:CoverageIgnore
val <TModel : Any, TDependency> Action<TModel, TDependency>.skip: Next<TModel, TDependency>
    get() = Next()

/**
 * Shorthand for returning an action result with both changes to the model and triggered effects.
 *
 * For example:
 *
 * ```kotlin
 * data object OnLoginButtonClick : MainScreenAction {
 *     override fun MainScreenModel.proceed() =
 *         if (username.isBlank() || password.isBlank()) {
 *             skip
 *         } else {
 *             outcome(copy(loading = true), StartLogin(username, password))
 *         }
 * }
 * ```
 */
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

/**
 * Shorthand for returning an action result with optional model changes only.
 *
 * For example:
 *
 * ```kotlin
 * data object OnDetailsExpandClick : HelpScreenAction {
 *     override fun HelpScreenModel.proceed() =
 *         mutate(copy(showDetails = !showDetails))
 * }
 * ```
 */
@Suppress("UnusedReceiverParameter")
@CoverageIgnore
fun <TModel : Any, TDependency> Action<TModel, TDependency>.mutate(model: TModel? = null): Next<TModel, TDependency> =
    Next(model = model)

/**
 * Shorthand for returning an action result with triggered effects only.
 *
 * For example:
 *
 * ```kotlin
 * data object OnSwipeRefresh : DetailScreenAction {
 *     override fun DetailScreenModel.proceed() =
 *         trigger(ReloadUserDetails)
 * }
 * ```
 */
@Suppress("UnusedReceiverParameter")
@CoverageIgnore
fun <TModel : Any, TDependency> Action<TModel, TDependency>.trigger(vararg effects: Effect<TModel, TDependency>): Next<TModel, TDependency> =
    Next(effects = effects.toSet())

typealias next<TModel, TDependency> = Next<TModel, TDependency>
