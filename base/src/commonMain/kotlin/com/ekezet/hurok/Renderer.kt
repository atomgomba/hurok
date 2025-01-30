package com.ekezet.hurok

/**
 * Interface for transforming the current [Loop] model into a new view state.
 *
 * For example:
 *
 * ```kotlin
 * class ScoreScreenRenderer : Renderer<ScoreScreenModel, ScoreScreenState> {
 *     fun renderState(model: ScoreScreenModel) = ScoreScreenState(
 *         playerName = model.user.nickname,
 *         score = model.score.roundToInt().toString(),
 *     )
 * }
 * ```
 *
 * @param TModel the type of the [Loop] model
 * @param TState the concrete type of the resulting [ViewState]
 */
fun interface Renderer<in TModel : Any, out TState : ViewState<*, *>> {
    /**
     * @param model current [Loop] model
     * @return a new state
     */
    fun renderState(model: TModel): TState
}
