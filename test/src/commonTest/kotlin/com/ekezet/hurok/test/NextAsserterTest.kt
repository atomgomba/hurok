package com.ekezet.hurok.test

import com.ekezet.hurok.Action.Next
import com.ekezet.hurok.ActionEmitter
import com.ekezet.hurok.next
import com.ekezet.hurok.skip
import com.ekezet.hurok.trigger
import kotlin.test.Test

class NextAsserterTest {
    @Test
    fun assertSkipped() {
        val testModel = TestModel()
        val testAction = object : TestAction {
            override fun TestModel.proceed(): Next<TestModel, TestDependency> = skip

        }

        val subject = NextAsserter(
            with(testAction) {
                testModel.proceed()
            }
        )

        subject.assertSkipped()
    }

    @Test
    fun assertModel() {
        val testModel = TestModel()
        val testAction = object : TestAction {
            override fun TestModel.proceed(): Next<TestModel, TestDependency> =
                next(copy(title = "Mutated"))

        }

        val subject = NextAsserter(
            with(testAction) {
                testModel.proceed()
            }
        )

        val expectedModel = TestModel(title = "Mutated")

        subject.assertModel(expectedModel)
    }

    @Test
    fun assertEffects() {
        val testModel = TestModel()
        val testEffects = arrayOf(
            object : TestEffect {
                override suspend fun ActionEmitter<TestModel, TestDependency>.trigger(dependency: TestDependency?): Any {
                    TODO("Not yet implemented")
                }
            }
        )
        val testAction = object : TestAction {
            override fun TestModel.proceed(): Next<TestModel, TestDependency> =
                trigger(effects = testEffects)

        }

        val subject = NextAsserter(
            with(testAction) {
                testModel.proceed()
            }
        )

        subject.assertEffects(testEffects.toSet())
    }

    @Test
    fun assertModelNotChanged() {
        val testModel = TestModel()
        val testAction = object : TestAction {
            override fun TestModel.proceed(): Next<TestModel, TestDependency> =
                next(null)

        }

        val subject = NextAsserter(
            with(testAction) {
                testModel.proceed()
            }
        )

        subject.assertModelNotChanged()
    }

    @Test
    fun assertNoEffects() {
        val testModel = TestModel()
        val testAction = object : TestAction {
            override fun TestModel.proceed(): Next<TestModel, TestDependency> =
                next(copy())

        }

        val subject = NextAsserter(
            with(testAction) {
                testModel.proceed()
            }
        )

        subject.assertNoEffects()
    }
}