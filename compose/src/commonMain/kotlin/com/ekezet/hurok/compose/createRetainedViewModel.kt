package com.ekezet.hurok.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ekezet.hurok.Action
import com.ekezet.hurok.LoopBuilder
import com.ekezet.hurok.test.CoverageIgnore
import kotlin.reflect.KClass

@Composable
@NonRestartableComposable
@CoverageIgnore
inline fun <TState : Any, reified TModel : Any, TArgs, TDependency, TAction : Action<TModel, TDependency>> createRetainedViewModel(
    builder: @DisallowComposableCalls LoopBuilder<TState, TModel, TArgs, TDependency, TAction>,
    args: TArgs? = null,
    key: String? = TModel::class.qualifiedName,
): LoopViewModel<TState, TModel, TArgs, TDependency, TAction> =
    viewModel<LoopViewModel<TState, TModel, TArgs, TDependency, TAction>>(
        key = key,
        factory = remember {
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
                    val loop = builder.build(args)
                    return LoopViewModel(loop = loop) as T
                }
            }
        },
    )
