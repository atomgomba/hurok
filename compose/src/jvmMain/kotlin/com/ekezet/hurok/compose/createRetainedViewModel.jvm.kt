package com.ekezet.hurok.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.NonRestartableComposable
import com.ekezet.hurok.Action
import com.ekezet.hurok.LoopBuilder
import com.ekezet.hurok.ViewState

@Composable
@NonRestartableComposable
actual inline fun <TState : ViewState<TModel, TDependency>, reified TModel : Any, TArgs, TDependency, TAction : Action<TModel, TDependency>> createRetainedViewModel(
    builder: @DisallowComposableCalls LoopBuilder<TState, TModel, TArgs, TDependency, TAction>,
    args: TArgs?,
    key: String?,
    onNewInstance: @DisallowComposableCalls () -> Unit
): LoopViewModel<TState, TModel, TArgs, TDependency, TAction> {
    if (key == null) {
        val loop = builder.build(args)
        onNewInstance()
        return LoopViewModel(loop)
    }
    @Suppress("UNCHECKED_CAST")
    var viewModel = LoopViewModelHolder.items[key] as? LoopViewModel<TState, TModel, TArgs, TDependency, TAction>
    if (viewModel == null) {
        val loop = builder.build(args)
        onNewInstance()
        viewModel = LoopViewModel(loop)
        LoopViewModelHolder.items[key] = viewModel
    }
    return viewModel
}

object LoopViewModelHolder {
    val items: MutableMap<String, LoopViewModel<*, *, *, *, *>> = HashMap()
}
