package com.ekezet.hurok.compose

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.ekezet.hurok.Action
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@SuppressLint("ComposableNaming")
@Suppress("UnusedReceiverParameter", "FunctionName")
@Composable
fun <TState : Any, TModel : Any, TArgs, TDependency, TAction : Action<TModel, TDependency>> LoopStateCollectors.WithLifecycle(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    context: CoroutineContext = EmptyCoroutineContext,
) =
    LoopStateCollector<TState, TModel, TArgs, TDependency, TAction> {
        it.collectAsStateWithLifecycle(
            lifecycleOwner = lifecycleOwner,
            minActiveState = minActiveState,
            context = context,
        )
    }
