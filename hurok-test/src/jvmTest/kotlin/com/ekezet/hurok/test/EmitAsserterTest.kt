package com.ekezet.hurok.test

import com.ekezet.hurok.skip
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class EmitAsserterTest {
    @Test
    fun assertActionsSuccess() = runTest {
        val testAction = object : TestAction {
            override fun TestModel.proceed() = skip
        }
        val testAction2 = object : TestAction {
            override fun TestModel.proceed() = skip
        }

        val subject = EmitAsserter<TestModel, TestDependency>()

        subject.emit(testAction)
        subject.emit(testAction2)

        subject matches {
            assertActions(listOf(testAction, testAction2))
        }
    }

    @Test
    fun assertActionsFailure() = runTest {
        val testAction = object : TestAction {
            override fun TestModel.proceed() = skip
        }

        val subject = EmitAsserter<TestModel, TestDependency>()

        subject.emit(testAction)

        assertFailsWith(AssertionError::class) {
            subject matches {
                assertActions(listOf(testAction, testAction))
            }
        }
    }

    @Test
    fun assertNoActionsSuccess() = runTest {
        val subject = EmitAsserter<TestModel, TestDependency>()

        subject matches {
            assertNoActions()
        }
    }

    @Test
    fun assertNoActionsFailure() = runTest {
        val testAction = object : TestAction {
            override fun TestModel.proceed() = skip
        }

        val subject = EmitAsserter<TestModel, TestDependency>()

        subject.emit(testAction)

        assertFailsWith(AssertionError::class) {
            subject matches {
                assertNoActions()
            }
        }
    }
}