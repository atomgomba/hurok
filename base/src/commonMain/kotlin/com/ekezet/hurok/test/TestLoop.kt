package com.ekezet.hurok.test

import com.ekezet.hurok.Action
import com.ekezet.hurok.AnyActionEmitter
import com.ekezet.hurok.DispatcherProvider
import com.ekezet.hurok.Effect
import com.ekezet.hurok.Loop
import com.ekezet.hurok.Renderer
import com.ekezet.hurok.ViewState
import kotlinx.coroutines.CoroutineDispatcher

data class TestModel(val title: String = "", val foobar: Boolean = false)

data class TestState(val title: String) : ViewState<TestModel, TestDependency>()

data class TestArgs(val title: String, val foobar: Boolean? = null)

class TestDependency(var childEmitter: TestChildLoop? = null)

interface TestAction : Action<TestModel, TestDependency>

interface TestEffect : Effect<TestModel, TestDependency>

class TestLoop(
    model: TestModel,
    renderer: Renderer<TestModel, TestDependency, TestState>,
    args: TestArgs? = null,
    firstAction: TestAction? = null,
    dependency: TestDependency? = null,
    effectDispatcher: CoroutineDispatcher = DispatcherProvider.IO,
) : Loop<TestState, TestModel, TestArgs, TestDependency, TestAction>(
    model,
    renderer,
    args,
    firstAction,
    dependency,
    effectDispatcher,
) {
    override fun TestModel.applyArgs(args: TestArgs): TestModel =
        copy(title = args.title, foobar = args.foobar ?: foobar)

    override fun TestDependency.onAddChildEmitter(child: AnyActionEmitter) = when (child) {
        is TestChildLoop -> childEmitter = child
        else -> Unit
    }
}

@CoverageIgnore
class TestChildLoop(
    model: TestModel,
    renderer: Renderer<TestModel, TestDependency, TestState>,
    args: TestArgs? = null,
    firstAction: TestAction? = null,
    dependency: TestDependency? = null,
    effectDispatcher: CoroutineDispatcher = DispatcherProvider.IO,
) : Loop<TestState, TestModel, TestArgs, TestDependency, TestAction>(
    model,
    renderer,
    args,
    firstAction,
    dependency,
    effectDispatcher,
)
