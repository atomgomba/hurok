package com.ekezet.hurok.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.NonRestartableComposable
import com.ekezet.hurok.Action
import com.ekezet.hurok.LoopBuilder
import com.ekezet.hurok.ViewState

@Composable
@NonRestartableComposable
expect inline fun <TState : ViewState<TModel, TDependency>, reified TModel : Any, TArgs, TDependency, TAction : Action<TModel, TDependency>> createRetainedViewModel(
    builder: @DisallowComposableCalls LoopBuilder<TState, TModel, TArgs, TDependency, TAction>,
    args: TArgs? = null,
    key: String? = TModel::class.qualifiedName,
    crossinline onNewInstance: @DisallowComposableCalls () -> Unit = {},
): LoopViewModel<TState, TModel, TArgs, TDependency, TAction>
