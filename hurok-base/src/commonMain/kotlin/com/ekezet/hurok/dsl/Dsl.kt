@file:CoverageIgnore

package com.ekezet.hurok.dsl

import com.ekezet.hurok.Action
import com.ekezet.hurok.ActionEmitter
import com.ekezet.hurok.DefaultEffectContext
import com.ekezet.hurok.Loop
import com.ekezet.hurok.Renderer
import com.ekezet.hurok.test.CoverageIgnore
import kotlin.coroutines.CoroutineContext

@ExperimentalLoopBuilderApi
interface BuilderScope<TState : Any, TModel : Any, TDependency> {
    fun onStart(block: ActionEmitter<TModel, TDependency>.() -> Unit)
    fun onRender(block: Renderer<TState, TModel>)
}

@ExperimentalLoopBuilderApi
internal open class BuilderScopeImpl<TState : Any, TModel : Any, TDependency> : BuilderScope<TState, TModel, TDependency> {
    var renderer: Renderer<TState, TModel>? = null
    var init: (ActionEmitter<TModel, TDependency>.() -> Unit)? = null

    override fun onStart(block: ActionEmitter<TModel, TDependency>.() -> Unit) {
        init = block
    }

    override fun onRender(block: Renderer<TState, TModel>) {
        renderer = block
    }
}

@ExperimentalLoopBuilderApi
fun <TState : Any, TModel : Any, TDependency, TAction : Action<TModel, TDependency>> loop(
    model: TModel,
    dependency: TDependency,
    effectContext: CoroutineContext = DefaultEffectContext,
    builder: BuilderScope<TState, TModel, TDependency>.() -> Unit,
): Loop<TState, TModel, Unit, TDependency, TAction> {
    val scope = BuilderScopeImpl<TState, TModel, TDependency>()

    scope.builder()

    return scope.run {
        object : Loop<TState, TModel, Unit, TDependency, TAction>(
            model = model,
            renderer = requireNotNull(renderer) { "renderer must not be null" },
            args = null,
            argsApplyer = null,
            dependency = dependency,
            effectContext = effectContext,
            onStart = init ?: {},
        ) {}
    }
}

@ExperimentalLoopBuilderApi
fun <TState : Any, TModel : Any, TAction : Action<TModel, Unit>> loop(
    model: TModel,
    effectContext: CoroutineContext = DefaultEffectContext,
    builder: BuilderScope<TState, TModel, Unit>.() -> Unit,
): Loop<TState, TModel, Unit, Unit, TAction> {
    val scope = BuilderScopeImpl<TState, TModel, Unit>()

    scope.builder()

    return scope.run {
        object : Loop<TState, TModel, Unit, Unit, TAction>(
            model = model,
            renderer = requireNotNull(renderer) { "renderer must not be null" },
            args = null,
            argsApplyer = null,
            dependency = null,
            effectContext = effectContext,
            onStart = init ?: {},
        ) {}
    }
}
