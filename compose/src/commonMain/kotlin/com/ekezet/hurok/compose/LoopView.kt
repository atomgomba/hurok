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
import com.ekezet.hurok.Args
import com.ekezet.hurok.LoopBuilder
import com.ekezet.hurok.ViewState
import kotlinx.coroutines.CoroutineScope

/**
 * Attach a [Loop](com.ekezet.hurok.Loop) to a `@Composable` block as a state receiver.
 *
 * For example:
 *
 * ```kotlin
 * @Composable
 * fun ScoreScreenView() {
 *     LoopView(ScoreScreenLoop) {
 *         Column {
 *             Text(text = playerName)
 *
 *             Text(text = score)
 *
 *             Button(onClick = { emit(OnUpdateScoreClick) }) {
 *                 Text(text = "Update score")
 *             }
 *         }
 *     }
 * }
 * ```
 *
 * @param builder used to create the underlying Loop
 * @param args arguments to pass to the Loop when created or to apply when the Loop already exists
 * @param parentEmitter parent emitter, usually another Loop instance
 * @param scope CoroutineScope for launching actions
 * @param key used for storing the ViewModel
 * @param loopStateCollector can provide different methods for collecting Loop state
 * @param content composable function used by the Loop
 * @throws IllegalStateException when [builder] result is not a Loop instance
 */
@Throws(IllegalStateException::class)
@Composable
@NonRestartableComposable
inline fun <TState : ViewState<TModel, TDependency>, reified TModel : Any, TArgs : Args<TModel>, TDependency, TAction : Action<TModel, TDependency>> LoopView(
    builder: @DisallowComposableCalls LoopBuilder<TState, TModel, TArgs, TDependency, TAction>,
    args: TArgs? = null,
    parentEmitter: AnyActionEmitter? = null,
    scope: CoroutineScope = rememberCoroutineScope(),
    key: String? = TModel::class.qualifiedName,
    loopStateCollector: LoopStateCollector<TState, TModel, TArgs, TDependency, TAction> = LoopStateCollectors.Standard(),
    crossinline content: @Composable TState.() -> Unit,
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
        val state by loopStateCollector(loop)
        state.content()
    }
}
