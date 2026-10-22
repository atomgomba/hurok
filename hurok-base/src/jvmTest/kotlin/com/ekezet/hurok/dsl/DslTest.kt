package com.ekezet.hurok.dsl

import com.ekezet.hurok.next
import com.ekezet.hurok.test.TestArgs
import com.ekezet.hurok.test.TestDependency
import com.ekezet.hurok.test.TestEffect
import com.ekezet.hurok.test.TestModel
import com.ekezet.hurok.test.TestState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExperimentalLoopBuilderApi
class DslTest {
    @Test
    fun `Loop without args should be created correctly`() = runTest {
        // given
        val builder = pureLoop<TestState, TestModel> {
            initModel = TestModel(title = "World")

            onRender { model ->
                TestState(result = "Say ${model.title}!")
            }
        }
        val loop = builder.build(null)

        // when
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            loop.startIn(this)
            loop.emit { next(copy(title = "hello")) }
        }

        // then
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            loop.state.drop(1).collect { expectedState ->
                assertEquals("Say hello!", expectedState.result)
            }
        }
    }

    @Test
    fun `Loop with args should be created correctly`() = runTest {
        // given
        val builder = pureLoop<TestState, TestModel, TestArgs> {
            initModel = TestModel(title = "World")

            onRender { model ->
                TestState(result = "Say ${model.title}!")
            }

            onStart {
                emit { next(copy(title = "hello")) }
            }

            onApplyArgs { args ->
                copy(title = args.title)
            }
        }
        val loop = builder.build(null)

        // when
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            loop.startIn(this)
            loop.applyArgs(TestArgs(title = "nothing"))
        }

        // then
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            loop.state.drop(1).collect { expectedState ->
                assertEquals("Say nothing!", expectedState.result)
            }
        }
    }

    @Test
    fun `Loop with dependency should be created correctly`() = runTest {
        // given
        val builder = loop<TestState, TestModel, TestDependency> {
            initModel = TestModel(title = "World")
            dependency = TestDependency()

            onRender { model ->
                TestState(result = "Say ${model.title}!")
            }
        }
        val loop = builder.build(null)

        // when
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            loop.startIn(this)
            loop.emit { next(null, setOf(TestEffect { emit { next(copy(title = "hello")) } })) }
        }

        // then
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            loop.state.drop(1).collect { expectedState ->
                assertEquals("Say hello!", expectedState.result)
            }
        }
    }

    @Test
    fun `Loop with args and dependency should be created correctly`() = runTest {
        // given
        val builder = loop<TestState, TestModel, TestArgs, TestDependency> {
            initModel = TestModel()
            dependency = TestDependency()

            onRender { model ->
                TestState(result = "Say ${model.title}!")
            }

            onApplyArgs { args ->
                copy(title = args.title, foobar = args.foobar ?: foobar)
            }
        }
        val loop = builder.build(TestArgs(title = "nothing", foobar = true))

        // when
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            loop.startIn(this)
            loop.applyArgs(TestArgs(title = "something"))
        }

        // then
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            loop.state.drop(1).collect { expectedState ->
                assertEquals("Say something!", expectedState.result)
            }
        }
    }

    @Test
    fun `Loop with args and dependency without dependency provided should fail on creation`() = runTest {
        // given
        val builder = loop<TestState, TestModel, TestArgs, TestDependency> {
            initModel = TestModel()

            onRender { model ->
                TestState(result = "Say ${model.title}!")
            }

            onApplyArgs { args ->
                copy(title = args.title, foobar = args.foobar ?: foobar)
            }
        }

        // then
        assertFailsWith<UninitializedPropertyAccessException> {
            builder.build(TestArgs(title = "nothing", foobar = true))
        }
    }
}
