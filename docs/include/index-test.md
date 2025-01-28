# Package com.ekezet.hurok.test

# Tools for testing hurok-based applications

## DSL for unit testing Action result

Functions for asserting [Action](com.ekezet.hurok.Action) results:

* [assertSkipped](com.ekezet.hurok.test.NextAsserter.assertSkipped) - assert no change at all
* [assertModel](com.ekezet.hurok.test.NextAsserter.assertModel) - assert the expected model change
* [assertEffects](com.ekezet.hurok.test.NextAsserter.assertEffects) - assert triggering the expected effect(s)
* [assertModelNotChanged](com.ekezet.hurok.test.NextAsserter.assertModelNotChanged) - assert the model not changed
* [assertNoEffects](com.ekezet.hurok.test.NextAsserter.assertNoEffects) - assert no effects triggered

Example:

```kotlin
@Test
fun `Making a move works correctly`() {
    // set up init model and expected model
    // ...

    initModel after OnMoveMade(position) matches {
        assertModel(expectedModel)
        assertNoEffects()
    }
}
```

## DSL for unit testing Effect

To test an [Effect](com.ekezet.hurok.Effect) inherit from [EffectTest] and mock the dependency if there's one.
Use [runWith](com.ekezet.hurok.test.EffectTest.runWith) to test and assert the subject against the mocked dependency.

Functions for asserting effect results:

* [assertActions](com.ekezet.hurok.test.EmitAsserter.assertActions) can be used to assert which actions were emitted
* [assertNoActions](com.ekezet.hurok.test.EmitAsserter.assertNoActions) can be used to assert that no actions ere
  emitted

Example:

```kotlin
class GameBoardEffectTest : EffectTest() {
    // ...

    @Test
    fun `WaitBeforeNextTurn works correctly`() = runTest {
        val nextMove: Position = mockk()

        dependency runWith WaitBeforeNextTurn(nextMove) matches {
            assertActions(listOf(OnMoveMade(nextMove)))
        }
    }
}
```

## Testing the Renderer

To unit test a [Renderer](com.ekezet.hurok.Renderer) prepare a model, instantiate the [Renderer](com.ekezet.hurok.Renderer) and call its [renderState()](com.ekezet.hurok.Renderer) method to get the expected state. Then you can use assert methods like `assertEquals()` on the result.
