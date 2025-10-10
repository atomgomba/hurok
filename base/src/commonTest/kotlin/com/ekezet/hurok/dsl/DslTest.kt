package com.ekezet.hurok.dsl

import com.ekezet.hurok.dsl.withArgs.loop
import com.ekezet.hurok.test.TestArgs
import com.ekezet.hurok.test.TestDependency
import com.ekezet.hurok.test.TestModel
import com.ekezet.hurok.test.TestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DslTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Loop without args should be created correctly`() = runTest {
        val model = TestModel(title = "World")
        val dependency = TestDependency()
        var initCalled = false

        var actualModel: TestModel? = null
        var firstState: TestState? = null

        val loop = loop(
            model = model,
            dependency = dependency,
            effectContext = Dispatchers.Unconfined,
        ) {
            onStart {
                initCalled = true
            }

            onRender { model ->
                actualModel = model
                TestState(result = "Hello, ${model.title}")
            }
        }

        backgroundScope.launch(UnconfinedTestDispatcher()) {
            loop.startIn(this)
        }

        backgroundScope.launch(UnconfinedTestDispatcher()) {
            firstState = loop.state.first()
        }

        assertTrue(initCalled)
        assertEquals(TestState(result = "Hello, World"), firstState)
        assertEquals(model, actualModel)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Loop with args should be created correctly`() = runTest {
        val model = TestModel(title = "World")
        val dependency = TestDependency()
        var initCalled = false

        var actualModel: TestModel? = null
        var firstState: TestState? = null

        val loop = loop(
            model = model,
            args = null,
            dependency = dependency,
            effectContext = Dispatchers.Unconfined,
        ) {
            onStart {
                initCalled = true
            }

            onNewArgs { args: TestArgs ->
                copy(title = args.title)
            }

            onRender { model ->
                actualModel = model
                TestState(result = "Hello, ${model.title}")
            }
        }

        backgroundScope.launch(UnconfinedTestDispatcher()) {
            loop.startIn(this)
        }

        backgroundScope.launch(UnconfinedTestDispatcher()) {
            firstState = loop.state.first()
        }

        assertTrue(initCalled)
        assertEquals(TestState(result = "Hello, World"), firstState)
        assertEquals(model, actualModel)
    }
}