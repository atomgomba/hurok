package com.ekezet.hurok.test

import com.ekezet.hurok.ActionEmitter
import kotlin.test.Test
import kotlin.test.assertFailsWith

class EffectTestingKtTest {
    @Test
    fun runWithFailure() {
        val testDependency = TestDependency()
        val testEffect = object : TestEffect {
            override suspend fun ActionEmitter<TestModel, TestDependency>.trigger(dependency: TestDependency?): Any? {
                TODO("Not yet implemented")
            }
        }

        val subject = object : EffectTest() {}

        assertFailsWith(NotImplementedError::class) {
            with(subject) {
                testDependency runWith testEffect matches {

                }
            }
        }
    }
}