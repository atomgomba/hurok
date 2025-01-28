# Package com.ekezet.hurok.compose

# Utilities for using hurok with Compose Multiplatform

This package contains the [LoopView] which can be used to attach a [Loop](com.ekezet.hurok.Loop) to a `@Composable` function. 

For example:

```kotlin
@Composable
fun ScoreScreenView() {
    LoopView(ScoreScreenLoop) {
        Column {
            Text(text = playerName)

            Text(text = score)

            Button(onClick = { emit(OnUpdateScoreClick) }) {
                Text(text = "Update score")
            }
        }
    }
}
```

In the above example `ScoreScreenLoop` is a [LoopBuilder](com.ekezet.hurok.LoopBuilder). A [LoopBuilder](com.ekezet.hurok.LoopBuilder)(com.ekezet.hurok.LoopBuilder) is used to create a [Loop](com.ekezet.hurok.Loop) by passing
optional arguments and is needed to re-create the given [Loop](com.ekezet.hurok.Loop) when the function leaves the composition (e.g. a
configuration change).

```kotlin
class ScoreScreenLoop(
    model: ScoreScreenModel,
    renderer: Renderer<ScoreScreenModel, ScoreScreenDependency, ScoreScreenState>,
    args: ScoreScreenArgs? = null,
    firstAction: ScoreScreenAction? = null,
    dependency: ScoreScreenDependency? = null,
    effectContext: CoroutineContext = DispatcherProvider.IO,
) : Loop<ScoreScreenState, ScoreScreenModel, ScoreScreenArgs, ScoreScreenDependency, ScoreScreenAction>(
    model,
    renderer,
    args,
    firstAction,
    dependency,
    effectContext,
) {
    companion object Builder :
        LoopBuilder<ScoreScreenState, ScorescreenModel, ScoreScreenArgs, ScoreScreenDependency, ScoreScreenAction> {
        override fun build(args: ScoreScreenArgs?) =
            ScoreScreenLoop(
                model = ScoreScreenModel(),
                renderer = ScoreScreenRenderer(),
                args = args,
            )
    }
}
```

By creating a builder, the [Loop](com.ekezet.hurok.Loop) can be re-created by the UI with the given input arguments whenever necessary.