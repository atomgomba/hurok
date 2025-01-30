# hurok

This is a framework library for developing applications on the JVM based on the unidirectional dataflow model.

```mermaid
flowchart LR
    A[First State] -->|Input| B{Action} -->|Mutate| C(Model) --> D[Renderer] -->|Derive| E[Next State] -.->|Input| B
    B -->|Trigger| F([Effect]) --> B
```

Please click here for [generated documentation](https://atomgomba.github.io/hurok/).

## How it looks like

This brief example is missing some glue code to provide a quick feel of the library mechanics.

```kotlin
data class ScoreScreenModel(
    val user: User? = null,
    val isLoading: Boolean = false,
    val throwable: Throwable? = null,
)

data class ScoreScreenState(
    val playerName: String,
    val score: String,
    val isLoading: Boolean,
    val errorMessage: String?,
) : ViewState<ScoreScreenModel, ScoreScreenDependency>

@Composable
fun ScoreScreenView(args: ScoreScreenArgs?) {
    LoopView(
        builder = ScoreScreenLoop,
        args = args,
        firstAction = OnLoopStart,
    ) {
        Column {
            if (isLoading) {
                Text("Loading...")
            } else if (errorMessage != null) {
                Text(errorMessage)

                Button(onClick = { emit(OnUpdateScoreClick) }) {
                    Text("Retry")
                }
            } else {
                Text(playerName)
                Text(score)

                Button(onClick = { emit(OnUpdateScoreClick) }) {
                    Text("Update score")
                }
            }
        }
    }
}

class ScoreScreenRenderer : Renderer<ScoreScreenModel, ScoreScreenDependency, ScoreScreenState> {
    fun renderState(model: ScoreScreenModel) = with(model) {
        ScoreScreenState(
            playerName = if (user == null) "N/A" else user.nickname,
            score = if (user == null) "N/A" else user.score.roundToInt().toString(),
            errorMessage = throwable?.run { message ?: "Unknown error" },
            isLoading = isLoading,
        )
    }
}

data object OnUpdateScoreClick : ScoreScreenAction {
    override fun ScoreScreenModel.proceed() =
        outcome(copy(isLoading = true, throwable = null), UpdateScore(user.id))
}

data class UpdateScoreSuccess(val user: User) : ScoreScreenAction {
    override fun ScoreScreenModel.proceed() =
        mutate(copy(user = user, isLoading = false))
}

data class UpdateScoreError(val throwable: Throwable) : ScoreScreenAction {
    override fun ScoreScreenModel.proceed() =
        mutate(copy(throwable = throwable, isLoading = false))
}

data class UpdateScore(val userId: String) : ScoreScreenEffect {
    override suspend fun ScoreScreenEmitter.trigger(dependency: ScoreScreenDependencv) {
        try {
            val user = scoreService.getUserScore(userId)
            emit(UpdateScoreSuccess(user))
        } catch (throwable: Throwable) {
            emit(UpdateScoreError(throwable))
        }
    }
}
```

## Parts

| Name      | Description                                        |
|-----------|----------------------------------------------------|
| Model     | Holds data for business logic                      |
| ViewState | UI state derived from the `Model`                  |
| Renderer  | Uses the `Model` to create new `State` for the UI  |
| Action    | Mutates the `Model` and can trigger (any) `Effect` |
| Effect    | Does background work and triggers (any) `Action`   |
| Loop      | Handles `Action` and `Effect`                      |

## Technologies

* Kotlin Multiplatform
* Kotlin Coroutines
* Compose Multiplatform
* Android SDK

## Example code

For code samples please see [Othello for Android](https://github.com/atomgomba/othello).
