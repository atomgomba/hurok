package com.ekezet.hurok

import com.ekezet.hurok.test.TestAction
import com.ekezet.hurok.test.TestDependency
import com.ekezet.hurok.test.TestModel
import kotlinx.coroutines.CoroutineScope
import kotlin.test.Test
import kotlin.test.assertEquals

class ViewStateTest {
    @Test
    fun `Should delegate emit call to emitter`() {
        var expectedAction: Action<*, *>? = null
        val testEmitter = object : ActionEmitter<TestModel, TestDependency> {
            override val scope: CoroutineScope
                get() = TODO("Not yet implemented")

            override fun emit(action: Action<TestModel, TestDependency>) {
                expectedAction = action
            }
        }
        val testAction = object : TestAction {
            override fun TestModel.proceed() = skip
        }

        val subject = object : ViewState<TestModel, TestDependency>() {}.apply {
            emitter = testEmitter
        }

        subject.emit(testAction)

        assertEquals(expectedAction, testAction)
    }

    @Test
    fun `Should not fail if emitter not set`() {
        val testAction = object : TestAction {
            override fun TestModel.proceed() = skip
        }

        val subject = object : ViewState<TestModel, TestDependency>() {}

        subject.emit(testAction)
    }
}
