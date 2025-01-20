package com.ekezet.hurok.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.ekezet.hurok.Action
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
inline fun <TState : ViewState<TModel, TDependency>, reified TModel : Any, TArgs, TDependency, TAction : Action<TModel, TDependency>> LoopView(
    builder: @DisallowComposableCalls LoopBuilder<TState, TModel, TArgs, TDependency, TAction>,
    args: TArgs? = null,
    parentEmitter: AnyActionEmitter? = null,
    scope: CoroutineScope = rememberCoroutineScope(),
    key: String? = TModel::class.qualifiedName,
    crossinline wrapper: @Composable TState.() -> Unit,
) {
    val vm = createRetainedViewModel(builder = builder, args = args, key = key)

    val loop = remember(key1 = key) {
        parentEmitter?.addChildEmitter(vm.loop)
        vm.loop.startIn(scope)
    }

    if (args != vm.args) {
        args?.let { loop.applyArgs(args) }
        vm.args = args
    }

    CompositionLocalProvider(LocalActionEmitter provides loop) {
        val state by loop.collectAsState()
        state.wrapper()
    }
}
