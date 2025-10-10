@file:CoverageIgnore

package com.ekezet.hurok.dsl

import com.ekezet.hurok.Action
import com.ekezet.hurok.ActionEmitter
import com.ekezet.hurok.DefaultEffectContext
import com.ekezet.hurok.Loop
import com.ekezet.hurok.Renderer
import com.ekezet.hurok.test.CoverageIgnore
import kotlin.coroutines.CoroutineContext

abstract class BuilderScope<TState : Any, TModel : Any, TDependency> {
    internal var renderer: Renderer<TState, TModel>? = null
    internal var init: (ActionEmitter<TModel, TDependency>.() -> Unit)? = null

    fun onStart(block: ActionEmitter<TModel, TDependency>.() -> Unit) {
        init = block
    }

    fun render(block: Renderer<TState, TModel>) {
        renderer = block
    }
}

fun <TState : Any, TModel : Any, TDependency, TAction : Action<TModel, TDependency>> loop(
    model: TModel,
    dependency: TDependency,
    effectContext: CoroutineContext = DefaultEffectContext,
    builder: BuilderScope<TState, TModel, TDependency>.() -> Unit,
): Loop<TState, TModel, Unit, TDependency, TAction> {
    val scope = object : BuilderScope<TState, TModel, TDependency>() {}

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

fun <TState : Any, TModel : Any, TAction : Action<TModel, Unit>> loop(
    model: TModel,
    effectContext: CoroutineContext = DefaultEffectContext,
    builder: BuilderScope<TState, TModel, Unit>.() -> Unit,
): Loop<TState, TModel, Unit, Unit, TAction> {
    val scope = object : BuilderScope<TState, TModel, Unit>() {}

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
