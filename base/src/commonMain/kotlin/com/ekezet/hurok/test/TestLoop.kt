package com.ekezet.hurok.test

import com.ekezet.hurok.Action
import com.ekezet.hurok.Args
import com.ekezet.hurok.DependencyContainer
import com.ekezet.hurok.DispatcherProvider
import com.ekezet.hurok.Effect
import com.ekezet.hurok.Loop
import com.ekezet.hurok.Renderer
import com.ekezet.hurok.ViewState
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
data class TestState(val title: String) : ViewState<TestModel, TestDependency>()

/**
 * Args used in library tests.
 */
data class TestArgs(val title: String, val foobar: Boolean? = null) : Args<TestModel> {
    override fun applyToModel(model: TestModel): TestModel =
        model.copy(title = title, foobar = foobar ?: model.foobar)
}

/**
 * Dependency used in library tests.
 */
class TestDependency(var childEmitter: TestChildLoop? = null) : DependencyContainer {
    override fun plus(dependency: Any) = when (dependency) {
        is TestChildLoop -> childEmitter = dependency
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
    renderer: Renderer<TestModel, TestState>,
    args: TestArgs? = null,
    firstAction: TestAction? = null,
    dependency: TestDependency? = null,
    effectContext: CoroutineContext = DispatcherProvider.IO,
) : Loop<TestState, TestModel, TestArgs, TestDependency, TestAction>(
    model,
    renderer,
    args,
    firstAction,
    dependency,
    effectContext,
)

/**
 * Child loop used in library tests.
 */
@CoverageIgnore
class TestChildLoop(
    model: TestModel,
    renderer: Renderer<TestModel, TestState>,
    args: TestArgs? = null,
    firstAction: TestAction? = null,
    dependency: TestDependency? = null,
    effectContext: CoroutineContext = DispatcherProvider.IO,
) : Loop<TestState, TestModel, TestArgs, TestDependency, TestAction>(
    model,
    renderer,
    args,
    firstAction,
    dependency,
    effectContext,
)
