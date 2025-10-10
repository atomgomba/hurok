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
import com.ekezet.hurok.compose.viewModel.createRetainedViewModel
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
 * @param childOf set of loops that serve as parents for this loop
 * @param scope CoroutineScope for launching actions
 * @param key used for storing the ViewModel
 * @param loopStateCollector can provide different methods for collecting Loop state
 * @param content composable function used by the Loop
 * @throws IllegalStateException when [builder] result is not a Loop instance
 */
@Throws(IllegalStateException::class)
@Composable
@NonRestartableComposable
inline fun <TState : Any, reified TModel : Any, TArgs, TDependency, TAction : Action<TModel, TDependency>> LoopView(
    builder: @DisallowComposableCalls LoopBuilder<TState, TModel, TArgs, TDependency, TAction>,
    args: TArgs? = null,
    childOf: Set<AnyActionEmitter> = emptySet(),
    scope: CoroutineScope = rememberCoroutineScope(),
    key: String? = TModel::class.simpleName,
    loopStateCollector: LoopStateCollector<TState, TModel, TArgs, TDependency, TAction> = LoopStateCollectors.Standard(),
    crossinline content: @Composable TState.(emit: (action: TAction) -> Unit) -> Unit,
) {
    val vm = createRetainedViewModel(builder = builder, args = args, key = key)

    val loop = remember(key1 = key) {
        vm.loop.apply {
            childOf.forEach(::attachTo)
            startIn(scope)
        }
    }

    args?.let { loop.applyArgs(args) }

    CompositionLocalProvider(LocalActionEmitter provides loop) {
        val state by loopStateCollector(loop)
        state.content(loop::emit)
    }
}
