package com.ekezet.hurok

/**
 * Interface for transforming the current [Loop] model into a new view state.
 *
 * For example:
 *
 * ```kotlin
 * class ScoreScreenRenderer : Renderer<ScoreScreenState, ScoreScreenModel> {
 *     fun renderState(model: ScoreScreenModel) = ScoreScreenState(
 *         playerName = model.user.nickname,
 *         score = model.score.roundToInt().toString(),
 *     )
 * }
 * ```
 *
 * @param TState the concrete type of the resulting state
 * @param TModel the type of the [Loop] model
 */
fun interface Renderer<out TState, in TModel : Any> {
    /**
     * @param model current [Loop] model
     * @return a new state
     */
    fun renderState(model: TModel): TState
}
