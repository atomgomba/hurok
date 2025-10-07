package com.ekezet.hurok.compose

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.node.Ref
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.ekezet.hurok.Action.Next
import com.ekezet.hurok.LoopBuilder
import com.ekezet.hurok.test.TestAction
import com.ekezet.hurok.test.TestArgs
import com.ekezet.hurok.test.TestChildLoop
import com.ekezet.hurok.test.TestDependency
import com.ekezet.hurok.test.TestLoop
import com.ekezet.hurok.test.TestModel
import com.ekezet.hurok.test.TestState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@OptIn(ExperimentalTestApi::class, ExperimentalCoroutinesApi::class)
class LoopViewKtTest {
    private lateinit var testDependency: TestDependency
    private lateinit var testViewModelStore: ViewModelStore

    private val testViewModelStoreOwner = object : ViewModelStoreOwner {
        override val viewModelStore: ViewModelStore
            get() = testViewModelStore
    }

    @BeforeTest
    fun setUp() {
        testDependency = TestDependency()
        testViewModelStore = ViewModelStore()
    }

    @Test
    fun `Initial state should be produced`() = runComposeUiTest {
        val testLoopBuilder = createTestLoopBuilder(
            model = TestModel(title = "Hello"),
        )

        setContent {
            CompositionLocalProvider(
                LocalViewModelStoreOwner provides testViewModelStoreOwner
            ) {
                LoopView(
                    builder = testLoopBuilder,
                    content = {
                        assertEquals("Hello, World!", title)
                    },
                )
            }
        }
    }

    @Test
    fun `Correct initial state with args should be produced`() = runComposeUiTest {
        val testLoopBuilder = createTestLoopBuilder(
            model = TestModel(title = "Hello"),
        )
        val testArgs = TestArgs(title = "Howdy")

        setContent {
            CompositionLocalProvider(
                LocalViewModelStoreOwner provides testViewModelStoreOwner
            ) {
                LoopView(
                    builder = testLoopBuilder,
                    args = testArgs,
                    content = {
                        assertEquals("Howdy, World!", title)
                    },
                )
            }
        }
    }

    @Test
    fun `Correct state should be produced when args change`() = runComposeUiTest {
        val testLoopBuilder = createTestLoopBuilder(
            model = TestModel(title = "Hello"),
        )
        val testArgs = TestArgs(title = "Howdy")
        var recompositions = 0

        setContent {
            val args: Ref<TestArgs> = remember { Ref<TestArgs>().apply { value = testArgs } }

            CompositionLocalProvider(
                LocalViewModelStoreOwner provides testViewModelStoreOwner
            ) {
                LoopView(
                    builder = testLoopBuilder,
                    args = args.value,
                    content = {
                        if (recompositions == 0) {
                            assertEquals("Howdy, World!", title)
                        } else if (recompositions == 1) {
                            assertEquals("Hey, World!", title)
                        }
                        recompositions += 1
                    },
                )
            }

            LaunchedEffect(Unit) {
                args.value = TestArgs(title = "Hey")
            }
        }

        assertEquals(1, recompositions)
    }

    @Test
    fun `Correct state should be produced when args change to null`() = runComposeUiTest {
        val testLoopBuilder = createTestLoopBuilder(
            model = TestModel(title = "Hello"),
        )
        val testArgs = TestArgs(title = "Howdy")

        setContent {
            var args by remember { mutableStateOf<TestArgs?>(testArgs) }

            CompositionLocalProvider(
                LocalViewModelStoreOwner provides testViewModelStoreOwner
            ) {
                LoopView(
                    builder = testLoopBuilder,
                    args = args,
                    content = {
                        assertEquals("Howdy, World!", title)
                    },
                )
            }

            LaunchedEffect(Unit) {
                args = null
            }
        }
    }

    @Test
    fun `Child emitter should be added to parent emitter`() = runComposeUiTest {
        val testLoop = createTestLoopBuilder(
            model = TestModel(title = "Hello"),
        ).build(null)
        val testChildLoopBuilder = createTestChildLoopBuilder()

        setContent {
            CompositionLocalProvider(
                LocalViewModelStoreOwner provides testViewModelStoreOwner
            ) {
                LoopView(
                    builder = testChildLoopBuilder,
                    parentEmitter = testLoop,
                    content = {
                        // do nothing
                    },
                )
            }
        }

        assertIs<TestChildLoop>(testDependency.childEmitter)
    }

    @Test
    fun `Correct state should be produced when action is emitted`() = runComposeUiTest {
        val testLoopBuilder = createTestLoopBuilder(
            model = TestModel(title = "Hello"),
        )
        var recompositions = 0

        setContent {
            CompositionLocalProvider(
                LocalViewModelStoreOwner provides testViewModelStoreOwner
            ) {
                LoopView(
                    builder = testLoopBuilder,
                    content = { emit ->
                        if (recompositions == 0) {
                            assertEquals("Hello, World!", title)
                        } else if (recompositions == 1) {
                            assertEquals("Ciao, World!", title)
                        }
                        if (recompositions == 0) {
                            emit(TestAction { Next(copy(title = "Ciao")) })
                        }
                        // FIXME: Suppressed false positive, will be fixed in Kotlin 2.3.0
                        // see: https://youtrack.jetbrains.com/projects/KT/issues/KT-78881/K2-False-positive-Assigned-value-is-never-read-in-composable-function
                        @Suppress("AssignedValueIsNeverRead")
                        recompositions += 1
                    },
                )
            }
        }
    }

    private fun createTestLoopBuilder(
        model: TestModel,
    ) =
        LoopBuilder<TestState, TestModel, TestArgs, TestDependency, TestAction> { args ->
            TestLoop(
                model = model,
                renderer = { model -> TestState(title = "${model.title}, World!") },
                args = args,
                firstAction = null,
                dependency = testDependency,
                effectContext = UnconfinedTestDispatcher(),
            )
        }


    private fun createTestChildLoopBuilder() =
        LoopBuilder<TestState, TestModel, TestArgs, TestDependency, TestAction> { args ->
            TestChildLoop(
                model = TestModel(),
                renderer = { model -> TestState(title = "${model.title}, World!") },
                args = args,
                firstAction = null,
                dependency = TestDependency(),
                effectContext = UnconfinedTestDispatcher(),
            )
        }
}