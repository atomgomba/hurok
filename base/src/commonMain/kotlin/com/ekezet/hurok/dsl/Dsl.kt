@file:CoverageIgnore

package com.ekezet.hurok.dsl

import com.ekezet.hurok.Action
import com.ekezet.hurok.ActionEmitter
import com.ekezet.hurok.ArgsApplyer
import com.ekezet.hurok.Loop
import com.ekezet.hurok.Renderer
import com.ekezet.hurok.test.CoverageIgnore
import com.ekezet.hurok.utils.Dispatchers
import kotlin.coroutines.CoroutineContext

abstract class BuilderScope<TState : Any, TModel : Any, TDependency> {
    internal var renderer: Renderer<TState, TModel>? = null
    internal var init: (ActionEmitter<TModel, TDependency>.() -> Unit)? = null

    fun onStart(block: ActionEmitter<TModel, TDependency>.() -> Unit) {
        init = block
    }

    fun renderer(block: Renderer<TState, TModel>) {
        renderer = block
    }
}

abstract class BuilderScopeForArgs<TState : Any, TModel : Any, TArgs, TDependency> :
    BuilderScope<TState, TModel, TDependency>() {
    internal var argsApplyer: ArgsApplyer<TModel, TArgs>? = null

    fun onNewArgs(block: ArgsApplyer<TModel, TArgs>) {
        argsApplyer = block
    }
}

@JvmName("loopWithDependency")
fun <TState : Any, TModel : Any, TDependency, TAction : Action<TModel, TDependency>> loop(
    model: TModel,
    dependency: TDependency?,
    effectContext: CoroutineContext = Dispatchers.IO,
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

fun <TState : Any, TModel : Any, TDependency, TAction : Action<TModel, TDependency>> loop(
    model: TModel,
    effectContext: CoroutineContext = Dispatchers.IO,
    builder: BuilderScope<TState, TModel, TDependency>.() -> Unit,
): Loop<TState, TModel, Unit, TDependency, TAction> = loop(model, null, effectContext, builder)

fun <TState : Any, TModel : Any, TArgs, TDependency : Any, TAction : Action<TModel, TDependency>> loop(
    model: TModel,
    args: TArgs,
    dependency: TDependency,
    effectContext: CoroutineContext = Dispatchers.IO,
    builder: BuilderScopeForArgs<TState, TModel, TArgs, TDependency>.() -> Unit,
): Loop<TState, TModel, TArgs, TDependency, TAction> {
    val scope = object : BuilderScopeForArgs<TState, TModel, TArgs, TDependency>() {}

    scope.builder()

    return scope.run {
        object : Loop<TState, TModel, TArgs, TDependency, TAction>(
            model = model,
            renderer = requireNotNull(renderer) { "renderer must not be null" },
            args = args,
            argsApplyer = requireNotNull(argsApplyer) { "argsApplyer must not be null" },
            dependency = dependency,
            effectContext = effectContext,
            onStart = init ?: {},
        ) {}
    }
}

@JvmName("loopWithArgs")
fun <TState : Any, TModel : Any, TArgs : Any, TAction : Action<TModel, Unit>> loop(
    model: TModel,
    args: TArgs,
    effectContext: CoroutineContext = Dispatchers.IO,
    builder: BuilderScopeForArgs<TState, TModel, TArgs, Unit>.() -> Unit,
): Loop<TState, TModel, TArgs, Unit, TAction> = loop(model, args, Unit, effectContext, builder)
