@file:CoverageIgnore

package com.ekezet.hurok.dsl

import com.ekezet.hurok.Action
import com.ekezet.hurok.ActionEmitter
import com.ekezet.hurok.ArgsApplyer
import com.ekezet.hurok.DefaultEffectContext
import com.ekezet.hurok.Loop
import com.ekezet.hurok.LoopBuilder
import com.ekezet.hurok.Renderer
import com.ekezet.hurok.test.CoverageIgnore
import kotlin.coroutines.CoroutineContext
import kotlin.jvm.JvmName

@ExperimentalLoopBuilderApi
sealed class AbstractLoopBuilderScope<TState : Any, TModel : Any, TArgs, TDependency> {
    lateinit var initModel: TModel
    abstract var dependency: TDependency
    var effectContext: CoroutineContext = DefaultEffectContext

    internal lateinit var renderer: Renderer<TState, TModel>
    internal var onStartEmitter: ActionEmitter<TModel, TDependency>.() -> Unit = {}

    fun onStart(block: ActionEmitter<TModel, TDependency>.() -> Unit) {
        onStartEmitter = block
    }

    fun onRender(block: Renderer<TState, TModel>) {
        renderer = block
    }
}

@ExperimentalLoopBuilderApi
class ArgsApplyerDsl<TModel : Any, TArgs : Any> {
    internal lateinit var argsApplyer: ArgsApplyer<TModel, TArgs>

    operator fun invoke(block: ArgsApplyer<TModel, TArgs>) {
        argsApplyer = block
    }
}

@ExperimentalLoopBuilderApi
class LoopBuilderScope<TState : Any, TModel : Any> : AbstractLoopBuilderScope<TState, TModel, Unit, Unit>() {
    override var dependency: Unit = Unit
}

@ExperimentalLoopBuilderApi
class LoopBuilderScopeWithDependency<TState : Any, TModel : Any, TDependency : Any> :
    AbstractLoopBuilderScope<TState, TModel, Unit, TDependency>() {
    override lateinit var dependency: TDependency
}

@ExperimentalLoopBuilderApi
class LoopBuilderScopeWithArgs<TState : Any, TModel : Any, TArgs : Any> :
    AbstractLoopBuilderScope<TState, TModel, TArgs, Unit>() {
    override var dependency: Unit = Unit
    val onApplyArgs = ArgsApplyerDsl<TModel, TArgs>()
}

@ExperimentalLoopBuilderApi
class LoopBuilderScopeWithArgsAndDependency<TState : Any, TModel : Any, TArgs : Any, TDependency : Any> :
    AbstractLoopBuilderScope<TState, TModel, TArgs, TDependency>() {
    override lateinit var dependency: TDependency
    val onApplyArgs = ArgsApplyerDsl<TModel, TArgs>()
}

@OptIn(ExperimentalLoopBuilderApi::class)
fun <TState : Any, TModel : Any> pureLoop(block: LoopBuilderScope<TState, TModel>.() -> Unit): LoopBuilder<TState, TModel, Unit, Unit, Action<TModel, Unit>> {
    val scope = LoopBuilderScope<TState, TModel>()
    scope.block()
    return LoopBuilder {
        object : Loop<TState, TModel, Unit, Unit, Action<TModel, Unit>>(
            model = checkNotNull(scope.initModel) { "initModel is null" },
            renderer = checkNotNull(scope.renderer) { "renderer is null" },
            onStart = scope.onStartEmitter,
            effectContext = scope.effectContext,
        ) {}
    }
}

@OptIn(ExperimentalLoopBuilderApi::class)
@JvmName("pureLoopWithArgs")
fun <TState : Any, TModel : Any, TArgs : Any> pureLoop(block: LoopBuilderScopeWithArgs<TState, TModel, TArgs>.() -> Unit): LoopBuilder<TState, TModel, TArgs, Unit, Action<TModel, Unit>> {
    val scope = LoopBuilderScopeWithArgs<TState, TModel, TArgs>()
    scope.block()
    return LoopBuilder { args ->
        object : Loop<TState, TModel, TArgs, Unit, Action<TModel, Unit>>(
            model = scope.initModel,
            renderer = scope.renderer,
            onStart = scope.onStartEmitter,
            args = args,
            argsApplyer = scope.onApplyArgs.argsApplyer,
            effectContext = scope.effectContext,
        ) {}
    }
}

@OptIn(ExperimentalLoopBuilderApi::class)
fun <TState : Any, TModel : Any, TDependency : Any> loop(block: LoopBuilderScopeWithDependency<TState, TModel, TDependency>.() -> Unit): LoopBuilder<TState, TModel, Unit, TDependency, Action<TModel, TDependency>> {
    val scope = LoopBuilderScopeWithDependency<TState, TModel, TDependency>()
    scope.block()
    return LoopBuilder {
        object : Loop<TState, TModel, Unit, TDependency, Action<TModel, TDependency>>(
            model = scope.initModel,
            renderer = scope.renderer,
            onStart = scope.onStartEmitter,
            dependency = scope.dependency,
            effectContext = scope.effectContext,
        ) {}
    }
}

@OptIn(ExperimentalLoopBuilderApi::class)
@JvmName("loopWithArgs")
fun <TState : Any, TModel : Any, TArgs : Any, TDependency : Any> loop(block: LoopBuilderScopeWithArgsAndDependency<TState, TModel, TArgs, TDependency>.() -> Unit): LoopBuilder<TState, TModel, TArgs, TDependency, Action<TModel, TDependency>> {
    val scope = LoopBuilderScopeWithArgsAndDependency<TState, TModel, TArgs, TDependency>()
    scope.block()
    return LoopBuilder { args ->
        object : Loop<TState, TModel, TArgs, TDependency, Action<TModel, TDependency>>(
            model = scope.initModel,
            renderer = scope.renderer,
            dependency = scope.dependency,
            onStart = scope.onStartEmitter,
            args = args,
            argsApplyer = scope.onApplyArgs.argsApplyer,
            effectContext = scope.effectContext,
        ) {}
    }
}
