package com.ekezet.hurok.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import com.ekezet.hurok.Action
import com.ekezet.hurok.Args
import com.ekezet.hurok.Loop
import com.ekezet.hurok.ViewState
import com.ekezet.hurok.firstState
import com.ekezet.hurok.test.CoverageIgnore

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
fun <TState : ViewState<TModel, TDependency>, TModel : Any, TArgs : Args<TModel>, TDependency, TAction : Action<TModel, TDependency>> Loop<TState, TModel, TArgs, TDependency, TAction>.collectAsState(): State<TState> =
    state.collectAsState(initial = firstState, context = scope.coroutineContext)
