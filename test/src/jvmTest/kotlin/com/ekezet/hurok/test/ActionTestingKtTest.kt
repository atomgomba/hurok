package com.ekezet.hurok.test

import com.ekezet.hurok.ActionEmitter
import com.ekezet.hurok.mutate
import com.ekezet.hurok.skip
import com.ekezet.hurok.trigger
import kotlin.test.Test
import kotlin.test.assertFailsWith

class ActionTestingKtTest {
    @Test
    fun assertSkippedSuccess() {
        val testModel = TestModel()
        val testAction = object : TestAction {
            override fun TestModel.proceed() = skip
        }

        testModel after testAction matches {
            assertSkipped()
        }
    }

    @Test
    fun assertSkippedFailure() {
        val testModel = TestModel()
        val testAction = object : TestAction {
            override fun TestModel.proceed() =
                mutate(copy())
        }

        assertFailsWith(AssertionError::class) {
            testModel after testAction matches {
                assertSkipped()
            }
        }
    }

    @Test
    fun assertModelSuccess() {
        val testModel = TestModel()
        val testAction = object : TestAction {
            override fun TestModel.proceed() =
                mutate(copy())
        }

        testModel after testAction matches {
            assertModel(testModel)
        }
    }

    @Test
    fun assertModelFailure() {
        val testModel = TestModel()
        val testAction = object : TestAction {
            override fun TestModel.proceed() =
                mutate(null)
        }

        assertFailsWith(AssertionError::class) {
            testModel after testAction matches {
                assertModel(testModel)
            }
        }
    }

    @Test
    fun assertModelOmittedFailure() {
        val testModel = TestModel()
        val testAction = object : TestAction {
            override fun TestModel.proceed() =
                mutate(copy())
        }

        assertFailsWith(AssertionError::class) {
            testModel after testAction matches {
            }
        }
    }

    @Test
    fun assertEffectsSuccess() {
        val testModel = TestModel()
        val testEffects = arrayOf(
            object : TestEffect {
                override suspend fun ActionEmitter<TestModel, TestDependency>.trigger(dependency: TestDependency?): Any? {
                    TODO("Not yet implemented")
                }
            }
        )
        val testAction = object : TestAction {
            override fun TestModel.proceed() =
                trigger(effects = testEffects)
        }

        testModel after testAction matches {
            assertEffects(testEffects.toSet())
        }
    }

    @Test
    fun assertEffectsFailure() {
        val testModel = TestModel()
        val testEffects = arrayOf(
            object : TestEffect {
                override suspend fun ActionEmitter<TestModel, TestDependency>.trigger(dependency: TestDependency?): Any? {
                    TODO("Not yet implemented")
                }
            }
        )
        val testAction = object : TestAction {
            override fun TestModel.proceed() =
                mutate(copy())
        }

        assertFailsWith(AssertionError::class) {
            testModel after testAction matches {
                assertEffects(testEffects.toSet())
            }
        }
    }

    @Test
    fun assertEffectsOmittedFailure() {
        val testModel = TestModel()
        val testEffects = arrayOf(
            object : TestEffect {
                override suspend fun ActionEmitter<TestModel, TestDependency>.trigger(dependency: TestDependency?): Any? {
                    TODO("Not yet implemented")
                }
            }
        )
        val testAction = object : TestAction {
            override fun TestModel.proceed() =
                trigger(effects = testEffects)
        }

        assertFailsWith(AssertionError::class) {
            testModel after testAction matches {
            }
        }
    }

    @Test
    fun assertModelNotChangedSuccess() {
        val testModel = TestModel()
        val testAction = object : TestAction {
            override fun TestModel.proceed() = skip
        }

        testModel after testAction matches {
            assertModelNotChanged()
        }
    }

    @Test
    fun assertModelNotChangedFailure() {
        val testModel = TestModel()
        val testAction = object : TestAction {
            override fun TestModel.proceed() =
                mutate(copy())
        }

        assertFailsWith(AssertionError::class) {
            testModel after testAction matches {
                assertModelNotChanged()
            }
        }
    }

    @Test
    fun assertNoEffectsSuccess() {
        val testModel = TestModel()
        val testAction = object : TestAction {
            override fun TestModel.proceed() =
                mutate(null)
        }

        testModel after testAction matches {
            assertNoEffects()
        }
    }

    @Test
    fun assertNoEffectsFailure() {
        val testModel = TestModel()
        val testEffects = arrayOf(
            object : TestEffect {
                override suspend fun ActionEmitter<TestModel, TestDependency>.trigger(dependency: TestDependency?): Any? {
                    TODO("Not yet implemented")
                }
            }
        )
        val testAction = object : TestAction {
            override fun TestModel.proceed() =
                trigger(effects = testEffects)
        }

        assertFailsWith(AssertionError::class) {
            testModel after testAction matches {
                assertNoEffects()
            }
        }
    }
}