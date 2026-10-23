package com.ekezet.hurok.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import com.ekezet.hurok.Action
import com.ekezet.hurok.Loop
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun interface LoopStateCollector<TState : Any, TModel : Any, TArgs, TDependency, TAction : Action<TModel, TDependency>> {
    @Composable
    operator fun invoke(loop: Loop<TState, TModel, TArgs, TDependency, TAction>): State<TState>
}

object LoopStateCollectors

@Suppress("UnusedReceiverParameter", "FunctionName")
fun <TState : Any, TModel : Any, TArgs, TDependency, TAction : Action<TModel, TDependency>> LoopStateCollectors.Standard(
    context: CoroutineContext = EmptyCoroutineContext
) =
    LoopStateCollector<TState, TModel, TArgs, TDependency, TAction> { it.collectAsState(context) }
