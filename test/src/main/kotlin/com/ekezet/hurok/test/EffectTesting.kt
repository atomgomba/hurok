package com.ekezet.hurok.test

import com.ekezet.hurok.Action
import com.ekezet.hurok.Effect
import com.ekezet.hurok.ActionEmitter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlin.test.assertContentEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
abstract class EffectTest(
    testDispatcher: TestDispatcher = UnconfinedTestDispatcher(),
    private val testScope: CoroutineScope = TestScope(testDispatcher),
) : CoroutineScope by testScope {
    infix fun <TModel : Any, TDependency> TDependency.runWith(
        effect: Effect<TModel, TDependency>,
    ) = EmitAsserter<TModel, TDependency>(testScope).apply {
        runBlocking {
            effect.run { trigger(this@runWith) }
        }
    }
}

class EmitAsserter<TModel : Any, TDependency> internal constructor(
    override val parentScope: CoroutineScope,
) : ActionEmitter<TModel, TDependency> {
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

    override fun <TChildModel : Any, TChildDependency> addChildEmitter(
        child: ActionEmitter<TChildModel, TChildDependency>,
    ) {
        // not used
    }
}

infix fun <TModel : Any, TDependency> EmitAsserter<TModel, TDependency>.matches(
    block: EmitAsserter<TModel, TDependency>.() -> Unit,
) {
    block()
}
