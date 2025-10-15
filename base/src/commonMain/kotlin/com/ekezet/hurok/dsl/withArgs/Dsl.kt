@file:CoverageIgnore

package com.ekezet.hurok.dsl.withArgs

import com.ekezet.hurok.Action
import com.ekezet.hurok.ArgsApplyer
import com.ekezet.hurok.DefaultEffectContext
import com.ekezet.hurok.Loop
import com.ekezet.hurok.dsl.BuilderScope
import com.ekezet.hurok.test.CoverageIgnore
import kotlin.coroutines.CoroutineContext

abstract class BuilderScopeWithArgs<TState : Any, TModel : Any, TArgs, TDependency> :
    BuilderScope<TState, TModel, TDependency>() {
    internal var argsApplyer: ArgsApplyer<TModel, TArgs>? = null

    fun onNewArgs(block: ArgsApplyer<TModel, TArgs>) {
        argsApplyer = block
    }
}

fun <TState : Any, TModel : Any, TArgs, TAction : Action<TModel, Unit>> loop(
    model: TModel,
    effectContext: CoroutineContext = DefaultEffectContext,
    builder: BuilderScopeWithArgs<TState, TModel, TArgs, Unit>.() -> Unit,
): Loop<TState, TModel, TArgs, Unit, TAction> {
    val scope = object : BuilderScopeWithArgs<TState, TModel, TArgs, Unit>() {}

    scope.builder()

    return scope.run {
        object : Loop<TState, TModel, TArgs, Unit, TAction>(
            model = model,
            renderer = requireNotNull(renderer) { "renderer must not be null" },
            args = null,
            argsApplyer = requireNotNull(argsApplyer) { "argsApplyer must not be null" },
            dependency = null,
            effectContext = effectContext,
            onStart = init ?: {},
        ) {}
    }
}

fun <TState : Any, TModel : Any, TArgs, TDependency : Any, TAction : Action<TModel, TDependency>> loop(
    model: TModel,
    args: TArgs,
    dependency: TDependency,
    effectContext: CoroutineContext = DefaultEffectContext,
    builder: BuilderScopeWithArgs<TState, TModel, TArgs, TDependency>.() -> Unit,
): Loop<TState, TModel, TArgs, TDependency, TAction> {
    val scope = object : BuilderScopeWithArgs<TState, TModel, TArgs, TDependency>() {}

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

fun <TState : Any, TModel : Any, TArgs : Any, TAction : Action<TModel, Unit>> loop(
    model: TModel,
    args: TArgs,
    effectContext: CoroutineContext = DefaultEffectContext,
    builder: BuilderScopeWithArgs<TState, TModel, TArgs, Unit>.() -> Unit,
): Loop<TState, TModel, TArgs, Unit, TAction> = loop(model, args, Unit, effectContext, builder)
