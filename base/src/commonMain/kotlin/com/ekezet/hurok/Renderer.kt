package com.ekezet.hurok

/**
 * Interface for transforming the current [Loop] model into a new view state.
 *
 * For example:
 *
 * ```kotlin
 * class ScoreScreenRenderer : Renderer<ScoreScreenModel, Unit, ScoreScreenState> {
 *     fun renderState(model: ScoreScreenModel) = ScoreScreenState(
 *         playerName = model.user.nickname,
 *         score = model.score.roundToInt().toString(),
 *     )
 * }
 * ```
 *
 * @param TModel the type of the [Loop] model
 * @param TDependency the type of the [Loop]'s dependency
 * @param TState the concrete type of the resulting [ViewState]
 */
fun interface Renderer<TModel : Any, TDependency, out TState : ViewState<TModel, TDependency>> {
    /**
     * @param model current [Loop] model
     * @return a new state
     */
    fun renderState(model: TModel): TState
}
