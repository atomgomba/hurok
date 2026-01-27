package com.ekezet.hurok.test

import com.ekezet.hurok.Action
import com.ekezet.hurok.Action.Next
import com.ekezet.hurok.Effect
import kotlin.test.Asserter
import kotlin.test.DefaultAsserter
import kotlin.test.assertEquals

/**
 * Asserter for action results.
 *
 * @param TModel
 * @param TDependency
 * @property next
 * @constructor Create empty Next asserter
 */
class NextAsserter<TModel : Any, TDependency> internal constructor(
    private val next: Next<TModel, TDependency>,
) : Asserter by DefaultAsserter {
    internal var isNotModelAsserted: Boolean = true
    internal var isNotEffectsAsserted: Boolean = true

    /**
     * Assert that the action didn't mutate the model or emit any effects.
     *
     * @param message
     */
    fun assertSkipped(message: String? = null) {
        assertEquals(Next(), next, message)
        isNotModelAsserted = false
        isNotEffectsAsserted = false
    }

    /**
     * Assert the resulting model mutated by the action.
     *
     * @param expected
     * @param message
     */
    fun assertModel(expected: TModel, message: String? = null) {
        assertEquals(expected, next.model, message)
        isNotModelAsserted = false
    }

    /**
     * Assert the effects emitted by the action.
     *
     * @param expected
     * @param message
     */
    fun assertEffects(
        expected: Set<Effect<TModel, TDependency>>, message: String? = null,
    ) {
        assertEquals(expected, next.effects, message)
        isNotEffectsAsserted = false
    }

    /**
     * Assert that the action didn't mutate the model.
     *
     * @param message
     */
    fun assertModelNotChanged(message: String? = null) {
        assertEquals(null, next.model, message)
        isNotModelAsserted = false
    }

    /**
     * Assert that the action didn't emit any effects.
     *
     * @param message
     */
    fun assertNoEffects(message: String? = null) {
        assertTrue(message, next.effects.isEmpty())
        isNotEffectsAsserted = false
    }
}

/**
 * Apply the action to a model.
 *
 * @param TModel
 * @param TDependency
 * @param action
 * @return
 */
infix fun <TModel : Any, TDependency> TModel.after(
    action: Action<TModel, TDependency>,
): Next<TModel, TDependency> = action.run { proceed() }

/**
 * Assert the result of an action.
 *
 * @param TModel
 * @param TDependency
 * @param block
 * @receiver
 */
infix fun <TModel : Any, TDependency> Next<TModel, TDependency>.matches(
    block: NextAsserter<TModel, TDependency>.(next: Next<TModel, TDependency>) -> Unit,
) {
    val asserter = NextAsserter(next = this)

    asserter.block(this)

    if (model != null && asserter.isNotModelAsserted) {
        throw AssertionError("Expected no model change, but was: $model")
    }

    if (effects.isNotEmpty() && asserter.isNotEffectsAsserted) {
        throw AssertionError("Expected no effects, but was: $effects")
    }
}
