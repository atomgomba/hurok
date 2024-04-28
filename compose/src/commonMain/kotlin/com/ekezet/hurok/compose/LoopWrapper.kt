package com.ekezet.hurok.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.ekezet.hurok.Action
import com.ekezet.hurok.ActionEmitter
import com.ekezet.hurok.AnyActionEmitter
import com.ekezet.hurok.LoopBuilder
import com.ekezet.hurok.ViewState
import kotlinx.coroutines.CoroutineScope

/**
 * Pass the `State` of a `Loop` to a @Composable block.
 *
 * @throws IllegalStateException when `builder` result is not a `Loop` instance
 */
@Throws(IllegalStateException::class)
@Composable
@NonRestartableComposable
inline fun <TState : ViewState<TModel, TDependency>, reified TModel : Any, TArgs, TDependency, TAction : Action<TModel, TDependency>> LoopWrapper(
    builder: @DisallowComposableCalls LoopBuilder<TState, TModel, TArgs, TDependency, TAction>,
    args: TArgs? = null,
    parentEmitter: AnyActionEmitter? = null,
    parentScope: CoroutineScope = parentEmitter?.parentScope ?: rememberCoroutineScope(),
    key: String? = TModel::class.qualifiedName,
    crossinline wrapper: @Composable TState.(emitter: ActionEmitter<TModel, TDependency>) -> Unit,
) {
    var applyArgs by remember { mutableStateOf(true) }

    val vm = createRetainedViewModel(builder = builder, args = args, key = key) {
        applyArgs = false
    }

    val loop = remember(key1 = key) {
        parentEmitter?.addChildEmitter(vm.loop)
        vm.loop.startIn(parentScope)
    }

    LaunchedEffect(key1 = args) {
        if (applyArgs) {
            args?.let { loop.applyArgs(it) }
        }
        applyArgs = true
    }

    CompositionLocalProvider(LocalActionEmitter provides loop) {
        val state by loop.collectAsState()
        state.wrapper(loop)
    }
}
