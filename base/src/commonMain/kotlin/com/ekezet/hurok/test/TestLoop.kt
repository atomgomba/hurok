package com.ekezet.hurok.test

import com.ekezet.hurok.Action
import com.ekezet.hurok.ActionEmitter
import com.ekezet.hurok.AnyActionEmitter
import com.ekezet.hurok.ArgsApplyer
import com.ekezet.hurok.Effect
import com.ekezet.hurok.HasActionEmitter
import com.ekezet.hurok.Loop
import com.ekezet.hurok.Renderer
import com.ekezet.hurok.utils.DispatcherProvider
import kotlin.coroutines.CoroutineContext

/**
 * Types used to test the library.
 */

/**
 * Model used in library tests.
 */
data class TestModel(val title: String = "", val foobar: Boolean = false)

/**
 * State used in library tests.
 */
data class TestState(val result: String)

/**
 * Args used in library tests.
 */
data class TestArgs(val title: String, val foobar: Boolean? = null)

/**
 * Dependency used in library tests.
 */
class TestDependency(var childEmitter: TestChildLoop? = null) : HasActionEmitter {
    override fun plus(other: AnyActionEmitter) = when (other) {
        is TestChildLoop -> childEmitter = other
        else -> Unit
    }
}

/**
 * Action used in library tests.
 */
fun interface TestAction : Action<TestModel, TestDependency>

/**
 * Effect used in library tests.
 */
fun interface TestEffect : Effect<TestModel, TestDependency>

/**
 * Loop used in library tests.
 */
class TestLoop(
    model: TestModel,
    renderer: Renderer<TestState, TestModel>,
    args: TestArgs? = null,
    onStart: ActionEmitter<TestModel, TestDependency>.() -> Unit = {},
    dependency: TestDependency? = null,
    effectContext: CoroutineContext = DispatcherProvider.IO,
) : Loop<TestState, TestModel, TestArgs, TestDependency, TestAction>(
    model = model,
    renderer = renderer,
    args = args,
    argsApplyer = ArgsApplyer { args -> copy(title = args.title, foobar = args.foobar ?: foobar) },
    onStart = onStart,
    dependency = dependency,
    effectContext = effectContext,
)

/**
 * Child loop used in library tests.
 */
@CoverageIgnore
class TestChildLoop(
    model: TestModel,
    renderer: Renderer<TestState, TestModel>,
    args: TestArgs? = null,
    onStart: ActionEmitter<TestModel, TestDependency>.() -> Unit = {},
    dependency: TestDependency? = null,
    effectContext: CoroutineContext = DispatcherProvider.IO,
) : Loop<TestState, TestModel, TestArgs, TestDependency, TestAction>(
    model = model,
    renderer = renderer,
    args = args,
    argsApplyer = null,
    onStart = onStart,
    dependency = dependency,
    effectContext = effectContext,
)
