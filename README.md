# hurok

This is a Kotlin Multiplatform framework library for developing applications on the JVM, JavaScript and Android based on
the unidirectional dataflow model. The basic idea can be considered as an MVI architecture with reducer, but without the reducer.

```mermaid
flowchart LR
    A[First State] -->|Input| B{Action} -->|Mutate| C(Model) --> D[Renderer] -->|Derive| E[Next State] -.->|Input| B
    B -->|Trigger| F([Effect]) --> B
```

Please click here for [generated documentation](https://atomgomba.github.io/hurok/).

## How it looks like

This brief example is missing some glue code to provide a quick feel of the library mechanics.

```kotlin
// Data model for business logic
data class ScoreScreenModel(
    val user: User? = null,
    val isLoading: Boolean = false,
    val throwable: Throwable? = null,
)

// State for representation
data class ScoreScreenState(
    val playerName: String,
    val score: String,
    val isLoading: Boolean,
    val errorMessage: String?,
)

// Renderer converts the model into a state
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

@Composable
fun ScoreScreenView(args: ScoreScreenArgs?) {
    // Wraps a loop as a @Composable
    LoopView(
        builder = ScoreScreenLoop,
        args = args,
    ) { emit ->
        Column {
            if (isLoading) {
                Text("Loading...")
            } else if (errorMessage != null) {
                Text(errorMessage)

                // Buttons emit an update action
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

// Action to start updating the score on user click
data object OnUpdateScoreClick : ScoreScreenAction {
    override fun ScoreScreenModel.proceed() =
        next(copy(isLoading = true, throwable = null), UpdateScore(user.id))
}

// Action on successful update
data class UpdateScoreSuccess(val user: User) : ScoreScreenAction {
    override fun ScoreScreenModel.proceed() =
        next(copy(user = user, isLoading = false))
}

// Action on update failure
data class UpdateScoreError(val throwable: Throwable) : ScoreScreenAction {
    override fun ScoreScreenModel.proceed() =
        next(copy(throwable = throwable, isLoading = false))
}

// Background side effect for updating the data
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

| Name        | Description                                        |
|-------------|----------------------------------------------------|
| State       | Loop state derived from the `Model`                |
| Model       | Holds data for business logic                      |
| Args        | A way to pass inputs to an existing `Loop`         |
| ArgsApplyer | Applies arguments to the `Model`                   |
| Renderer    | Uses the `Model` to create new `State`             |
| Action      | Mutates the `Model` and can trigger (any) `Effect` |
| Effect      | Does background work and triggers (any) `Action`   |
| Loop        | Glue that holds the parts together                 |

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
    // core multiplatform package (added transitively when using the compose package below)
    implementation("com.ekezet.hurok:base:3.1.0")
    // library for using hurok with Compose Multiplatform
    implementation("com.ekezet.hurok:compose:3.1.0")
    // library for testing hurok-based applications
    testImplementation("com.ekezet.hurok:test:3.1.0")
}
```

### Version catalog

```toml
[versions]
hurok = "3.1.0"

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
