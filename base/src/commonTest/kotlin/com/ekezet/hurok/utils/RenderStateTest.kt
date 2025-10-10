package com.ekezet.hurok.utils

import com.ekezet.hurok.Renderer
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
            result = "Hello, World!",
        )

        val state = renderState(::TestLoop, testModel, Renderer { TestState(result = it.title + ", World!") })

        assertEquals(expectedState, state)
    }
}