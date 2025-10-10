package com.ekezet.hurok.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import com.ekezet.hurok.Action
import com.ekezet.hurok.Loop
import com.ekezet.hurok.test.CoverageIgnore
import kotlinx.coroutines.CoroutineScope

/**
 * Collects state values from this Loop as a State<TState>.
 *
 * @param TState
 * @param TModel
 * @param TArgs
 * @param TDependency
 * @param TAction
 * @return
 */
@Composable
@CoverageIgnore
fun <TState : Any, TModel : Any, TArgs : TModel, TDependency, TAction : Action<TModel, TDependency>> Loop<TState, TModel, TArgs, TDependency, TAction>.collectAsState(
    scope: CoroutineScope = rememberCoroutineScope(),
): State<TState> =
    state.collectAsState(initial = latestState, context = scope.coroutineContext)
