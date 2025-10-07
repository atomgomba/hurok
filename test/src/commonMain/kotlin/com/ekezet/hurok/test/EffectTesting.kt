package com.ekezet.hurok.test

import com.ekezet.hurok.Action
import com.ekezet.hurok.ActionEmitter
import com.ekezet.hurok.Effect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlin.coroutines.CoroutineContext
import kotlin.test.assertContentEquals
import kotlin.test.assertTrue

/**
 * Base class for Effect tests.
 *
 * @property testScope
 * @constructor
 *
 * @param testContext
 */
@OptIn(ExperimentalCoroutinesApi::class)
abstract class EffectTest(
    testContext: CoroutineContext = UnconfinedTestDispatcher(),
    private val testScope: CoroutineScope = TestScope(testContext),
) : CoroutineScope by testScope {

    /**
     * Run the effect using the dependency for asserting the result.
     *
     * @param TModel
     * @param TDependency
     * @param effect
     */
    infix fun <TModel : Any, TDependency> TDependency.runWith(
        effect: Effect<TModel, TDependency>,
    ) = EmitAsserter<TModel, TDependency>().apply {
        runBlocking {
            effect.run { trigger(this@runWith) }
        }
    }
}

/**
 * Action emitter used for testing.
 *
 * @param TModel
 * @param TDependency
 * @constructor Create empty Emit asserter
 */
class EmitAsserter<TModel : Any, TDependency> internal constructor() : ActionEmitter<TModel, TDependency> {
    private val emitted: MutableList<Action<TModel, TDependency>> = mutableListOf()

    override fun emit(action: Action<TModel, TDependency>) {
        emitted.add(action)
    }

    fun assertActions(actions: List<Action<TModel, TDependency>>, message: String? = null) {
        assertContentEquals(actions, emitted, message)
    }

    fun assertNoActions(message: String? = null) {
        assertTrue(emitted.isEmpty(), message)
    }
}

/**
 * Assert emitted actions by the [Effect].
 *
 * @param TModel
 * @param TDependency
 * @param block
 * @receiver
 */
inline infix fun <TModel : Any, TDependency> EmitAsserter<TModel, TDependency>.matches(
    crossinline block: EmitAsserter<TModel, TDependency>.() -> Unit,
) {
    block()
}
