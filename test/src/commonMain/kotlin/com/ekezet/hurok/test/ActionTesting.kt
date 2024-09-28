package com.ekezet.hurok.test

import com.ekezet.hurok.Action
import com.ekezet.hurok.Action.Next
import com.ekezet.hurok.Effect
import kotlin.test.Asserter
import kotlin.test.DefaultAsserter
import kotlin.test.assertEquals

class NextAsserter<TModel : Any, TDependency> internal constructor(
    private val next: Next<TModel, TDependency>,
) : Asserter by DefaultAsserter {
    internal var isNotModelAsserted: Boolean = true
    internal var isNotEffectsAsserted: Boolean = true

    fun assertSkipped(message: String? = null) {
        assertEquals(Next(), next, message)
        isNotModelAsserted = false
        isNotEffectsAsserted = false
    }

    fun assertModel(expected: TModel, message: String? = null) {
        assertEquals(expected, next.model, message)
        isNotModelAsserted = false
    }

    fun assertEffects(
        expected: Set<Effect<TModel, TDependency>>, message: String? = null,
    ) {
        assertEquals(expected, next.effects, message)
        isNotEffectsAsserted = false
    }

    fun assertModelNotChanged(message: String? = null) {
        assertEquals(null, next.model, message)
        isNotModelAsserted = false
    }

    fun assertNoEffects(message: String? = null) {
        assertTrue(message, next.effects.isEmpty())
        isNotEffectsAsserted = false
    }
}

infix fun <TModel : Any, TDependency> TModel.after(
    action: Action<TModel, TDependency>,
): Next<TModel, TDependency> = action.run { proceed() }

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
