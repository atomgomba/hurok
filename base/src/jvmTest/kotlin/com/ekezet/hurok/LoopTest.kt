package com.ekezet.hurok

import com.ekezet.hurok.test.TestAction
import com.ekezet.hurok.test.TestArgs
import com.ekezet.hurok.test.TestChildLoop
import com.ekezet.hurok.test.TestDependency
import com.ekezet.hurok.test.TestEffect
import com.ekezet.hurok.test.TestLoop
import com.ekezet.hurok.test.TestModel
import com.ekezet.hurok.test.TestState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class LoopTest {
    @Test
    fun `Renderer should produce expected state`() = runTest {
        val testModel = TestModel(title = "Hello")

        val subject = TestLoop(
            model = testModel,
            renderer = { model ->
                TestState(result = "${model.title}, World!")
            },
        )

        val expectedState = TestState(
            result = "Hello, World!",
        )

        backgroundScope.launch(UnconfinedTestDispatcher()) {
            assertEquals(expectedState, subject.state.last())
        }
    }

    @Test
    fun `Renderer should produce expected state when providing args`() = runTest {
        val testModel = TestModel(title = "")
        val testArgs = TestArgs(title = "Hey")

        val subject = TestLoop(
            model = testModel,
            renderer = { model ->
                TestState(result = "${model.title}, World!")
            },
            args = testArgs,
        )

        val expectedState = TestState(
            result = "Hey, World!",
        )

        backgroundScope.launch(UnconfinedTestDispatcher()) {
            assertEquals(expectedState, subject.state.last())
        }
    }

    @Test
    fun `Renderer should produce expected state when providing a first Action`() = runTest {
        val testModel = TestModel(title = "")

        val subject = TestLoop(
            model = testModel,
            renderer = { model ->
                TestState(result = "${model.title}, World!")
            },
            onStart = { emit(TestAction { next(copy(title = "Howdy")) }) },
        )

        val expectedState = TestState(
            result = "Howdy, World!",
        )

        backgroundScope.launch(UnconfinedTestDispatcher()) {
            subject.state.drop(1).collect { state ->
                assertEquals(expectedState, state)
            }
        }
    }

    @Test
    fun `Renderer should produce expected state when applying args`() = runTest {
        val testModel = TestModel(title = "Hello")
        val testArgs = TestArgs(title = "Hey")

        val subject = TestLoop(
            model = testModel,
            renderer = { model ->
                TestState(result = "${model.title}, World!")
            },
        )

        subject.applyArgs(testArgs)

        val state = subject.state.first()

        val expectedState = TestState(
            result = "Hey, World!",
        )

        assertEquals(expectedState, state)
    }

    @Test
    fun `Renderer should produce expected state when mutate Action emitted`() = runTest {
        val testModel = TestModel(title = "Hello")

        val subject = TestLoop(
            model = testModel,
            renderer = { model ->
                TestState(result = "${model.title}, World!")
            },
        )

        backgroundScope.launch(UnconfinedTestDispatcher()) {
            subject.startIn(this)
            subject.emit {
                next(copy(title = "Foobar"))
            }
        }

        val expectedState = TestState(
            result = "Foobar, World!",
        )

        backgroundScope.launch(UnconfinedTestDispatcher()) {
            subject.state.collect { state ->
                assertEquals(expectedState, state)
            }
        }
    }

    @Test
    fun `Renderer should produce expected state when trigger Action emitted`() = runTest {
        val testModel = TestModel(title = "Hello")
        val testEffect = object : TestEffect {
            override suspend fun ActionEmitter<TestModel, TestDependency>.trigger(dependency: TestDependency?) =
                emit(object : TestAction {
                    override fun TestModel.proceed() =
                        mutate(copy(title = "Triggered"))
                })
        }
        val testAction = object : TestAction {
            override fun TestModel.proceed() =
                trigger(testEffect)
        }

        val subject = TestLoop(
            model = testModel,
            renderer = { model ->
                TestState(result = "${model.title}, World!")
            },
            onStart = { emit(testAction) },
        )

        backgroundScope.launch(UnconfinedTestDispatcher()) {
            subject.startIn(this)
        }

        val expectedState = TestState(
            result = "Triggered, World!",
        )

        backgroundScope.launch(UnconfinedTestDispatcher()) {
            subject.state.drop(1).collect { state ->
                assertEquals(expectedState, state)
            }
        }
    }

    @Test
    fun `Renderer should produce expected state when outcome Action emitted`() = runTest {
        val testModel = TestModel(title = "Hello")
        val testEffect = object : TestEffect {
            override suspend fun ActionEmitter<TestModel, TestDependency>.trigger(dependency: TestDependency?) =
                emit(object : TestAction {
                    override fun TestModel.proceed() =
                        mutate(copy(title = "Triggered"))
                })
        }
        val testAction = object : TestAction {
            override fun TestModel.proceed() =
                outcome(copy(), testEffect)
        }

        val subject = TestLoop(
            model = testModel,
            renderer = { model ->
                TestState(result = "${model.title}, World!")
            },
            onStart = { emit(testAction) },
        )

        backgroundScope.launch(UnconfinedTestDispatcher()) {
            subject.startIn(this)
        }

        val expectedState = TestState(
            result = "Triggered, World!",
        )

        backgroundScope.launch(UnconfinedTestDispatcher()) {
            subject.state.drop(1).collect { state ->
                assertEquals(expectedState, state)
            }
        }
    }

    @Test
    fun `Renderer should produce expected state when args applied and mutate Action emitted`() = runTest {
        val testModel = TestModel(title = "Hello")
        val testArgs = TestArgs(title = "Hey", foobar = true)
        val testAction = object : TestAction {
            override fun TestModel.proceed() =
                mutate(copy(title = "Howdy"))
        }

        val subject = TestLoop(
            model = testModel,
            renderer = { model ->
                TestState(
                    result = if (model.foobar) "Foobar, World!" else "${model.title}, World!",
                )
            },
            args = testArgs,
        )

        backgroundScope.launch(UnconfinedTestDispatcher()) {
            subject.startIn(this)
            subject.emit(testAction)
        }

        val expectedState = TestState(
            result = "Foobar, World!",
        )

        backgroundScope.launch(UnconfinedTestDispatcher()) {
            subject.state.collect { state ->
                assertEquals(expectedState, state)
            }
        }
    }

    @Test
    fun `Emit should throw error when Loop not started`() = runTest {
        val testModel = TestModel()
        val testAction = object : TestAction {
            override fun TestModel.proceed() = skip
        }

        val subject = TestLoop(
            model = testModel,
            renderer = { TestState(result = "") },
        )

        backgroundScope.launch(UnconfinedTestDispatcher()) {
            assertFailsWith(IllegalStateException::class) {
                subject.emit(testAction)
            }
        }
    }

    @Test
    fun `attachTo should fail when parent dependency not set`() = runTest {
        val testModel = TestModel()

        val subject = TestLoop(
            model = testModel,
            renderer = { TestState(result = "") },
        )

        val childEmitter = TestChildLoop(
            model = testModel,
            renderer = { TestState(result = "") },
        )

        assertFailsWith(IllegalArgumentException::class) {
            subject.attachTo(childEmitter)
        }
    }

    @Test
    fun `attachTo should update dependency`() = runTest {
        val testModel = TestModel()
        val testDependency = TestDependency()

        val subject = TestLoop(
            model = testModel,
            renderer = { TestState(result = "") },
            dependency = testDependency,
        )

        val childEmitter = TestChildLoop(
            model = testModel,
            renderer = { TestState(result = "") },
        )

        childEmitter.attachTo(subject)

        assertEquals(childEmitter, testDependency.childEmitter)
    }
}
