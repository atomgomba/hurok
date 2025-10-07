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
)

@Composable
fun ScoreScreenView(args: ScoreScreenArgs?) {
    LoopView(
        builder = ScoreScreenLoop,
        args = args,
    ) { emit ->
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

class ScoreScreenRenderer : Renderer<ScoreScreenState, ScoreScreenModel> {
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

| Name     | Description                                        |
|----------|----------------------------------------------------|
| State    | Loop state derived from the `Model`                |
| Model    | Holds data for business logic                      |
| Renderer | Uses the `Model` to create new `State` for the UI  |
| Action   | Mutates the `Model` and can trigger (any) `Effect` |
| Effect   | Does background work and triggers (any) `Action`   |
| Loop     | Glue that holds the parts together                 |

## Using hurok with Gradle

Packages are published to Maven Central, so make sure to add it to your list of repositories.

```kts
repositories {
    mavenCentral()
}
```

### Kotlin DSL

```kotlin
dependencies {
    // core multiplatform package
    implementation("com.ekezet.hurok:base:2.3.0")
    // library for using hurok with Compose Multiplatform
    implementation("com.ekezet.hurok:compose:2.3.0")
    // library for testing hurok-based applications
    testImplementation("com.ekezet.hurok:test:2.3.0")
}
```

### Version catalog

```toml
[versions]
hurok = "2.3.0"

[libraries]
hurok-base = { group = "com.ekezet.hurok", name = "base", version.ref = "hurok" }
hurok-compose = { group = "com.ekezet.hurok", name = "compsoe", version.ref = "hurok" }
hurok-test = { group = "com.ekezet.hurok", name = "test", version.ref = "hurok" }

[bundles]
hurok = ["hurok-base", "hurok-compose"]
```

```kotlin
dependencies {
    implementation(libs.bundles.hurok)
    testImplementation(libs.hurok.test)
}
```

## Technologies

* Kotlin Multiplatform
* Kotlin Coroutines
* Compose Multiplatform
* Android SDK

## Example code

For code samples please see [Othello for Android](https://github.com/atomgomba/othello).
