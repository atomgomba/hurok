package com.ekezet.hurok

fun interface Renderer<TModel : Any, TDependency, out TState : ViewState<TModel, TDependency>> {
    /**
     * Render the model into state.
     */
    fun renderState(model: TModel): TState
}
