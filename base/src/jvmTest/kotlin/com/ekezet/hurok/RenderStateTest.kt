package com.ekezet.hurok

import com.ekezet.hurok.test.TestLoop
import com.ekezet.hurok.test.TestModel
import com.ekezet.hurok.test.TestState
import kotlin.test.Test
import kotlin.test.assertEquals

class RenderStateTest {
    @Test
    fun `Should render the expected state`() {
        val testModel = TestModel(title = "Hello")

        val expectedState = TestState(
            title = "Hello, World!",
        )

        val state = renderState(::TestLoop, testModel, Renderer { TestState(title = it.title + ", World!") })

        assertEquals(expectedState, state)
    }
}