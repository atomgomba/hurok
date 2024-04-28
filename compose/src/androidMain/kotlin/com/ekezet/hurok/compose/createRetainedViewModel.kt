package com.ekezet.hurok.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ekezet.hurok.Action
import com.ekezet.hurok.Loop
import com.ekezet.hurok.LoopBuilder
import com.ekezet.hurok.ViewState

@Composable
@NonRestartableComposable
actual inline fun <TState : ViewState<TModel, TDependency>, reified TModel : Any, TArgs, TDependency, TAction : Action<TModel, TDependency>> createRetainedViewModel(
    builder: @DisallowComposableCalls LoopBuilder<TState, TModel, TArgs, TDependency, TAction>,
    args: TArgs?,
    key: String?,
    crossinline onNewInstance: @DisallowComposableCalls () -> Unit,
): LoopViewModel<TState, TModel, TArgs, TDependency, TAction> =
    viewModel<LoopViewModel<TState, TModel, TArgs, TDependency, TAction>>(
        key = key,
        factory = remember {
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val loop =
                        builder.build(args) as? Loop<TState, TModel, TArgs, TDependency, TAction>
                            ?: error("Not a Loop instance")
                    onNewInstance()
                    return LoopViewModel(loop = loop) as T
                }
            }
        },
    )
