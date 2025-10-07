package com.ekezet.hurok

import com.ekezet.hurok.utils.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * Base class that handles unidirectional data flow.
 *
 * @param model the initial model
 * @param renderer transform model into state
 * @param args input arguments
 * @param argsApplyer method to apply new args to the model
 * @param onStart block to run before beginning to collect the loop actions
 * @param dependency dependency container
 * @param effectContext dispatcher for launching effects
 */
abstract class Loop<out TState : Any, TModel : Any, in TArgs, TDependency, in TAction : Action<TModel, TDependency>>(
    model: TModel,
    renderer: Renderer<TState, TModel>,
    args: TArgs? = null,
    private val argsApplyer: ArgsApplyer<TModel, TArgs>? = null,
    private val onStart: ActionEmitter<TModel, TDependency>.() -> Unit = {},
    internal val dependency: TDependency? = null,
    private val effectContext: CoroutineContext = Dispatchers.IO,
) : ActionEmitter<TModel, TDependency> {

    private val currentModel = MutableStateFlow(
        model.run {
            if (argsApplyer == null || args == null) {
                this
            } else {
                with(argsApplyer) { applyArgs(args) }
            }
        },
    )

    /**
     * The current state produced by this loop
     */
    val state: Flow<TState> = currentModel
        .map(renderer::renderState)
        .distinctUntilChanged()

    private val actions = MutableSharedFlow<TAction>()

    private lateinit var _scope: CoroutineScope
    private val scope: CoroutineScope
        @Throws(IllegalArgumentException::class)
        get() = if (!::_scope.isInitialized) {
            error("Loop must be started. Please call startIn() first!")
        } else {
            _scope
        }

    private var lastArgs: TArgs? = args

    final override fun emit(action: Action<TModel, TDependency>) {
        scope.launch {
            @Suppress("UNCHECKED_CAST") actions.emit(action as TAction)
        }
    }

    /**
     * Apply new arguments to this loop.
     *
     * @param args new arguments
     * @return the loop
     * @throws IllegalStateException if [argsApplyer] is null
     */
    @Synchronized
    fun applyArgs(args: TArgs & Any): Loop<TState, TModel, TArgs, TDependency, TAction> {
        requireNotNull(argsApplyer) { "argsApplyer must not be null" }
        if (lastArgs == args) {
            return this
        }
        lastArgs = args
        currentModel.update { model ->
            with(argsApplyer) { model.applyArgs(args) }
        }
        return this
    }

    /**
     * Start the loop in the given scope.
     *
     * @param scope the scope used to launch actions
     * @return the coroutine [Job] launched
     */
    @Throws(IllegalArgumentException::class)
    fun startIn(scope: CoroutineScope): Job {
        if (::_scope.isInitialized && _scope.isActive) {
            scope.cancel()
        }
        _scope = scope
        return scope.launch {
            actions
                .onStart { onStart() }
                .collect(::onNextAction)
        }
    }

    /**
     * Attach this loop as a child to another loop.
     *
     * The dependency of the parent loop must implement [HasActionEmitter] for this to work.
     *
     * @param parent loop serving as the parent
     */
    fun attachTo(parent: AnyActionEmitter) {
        val container = (parent as? AnyLoop)?.dependency as? HasActionEmitter
        requireNotNull(container) { "to attach this loop as a child the dependency of the parent must implement HasChildLoop" }
        container + this
    }

    private suspend fun onNextAction(action: TAction) = coroutineScope {
        val (updatedModel, triggeredEffects) = action.run { currentModel.value.proceed() }
        updatedModel?.let { new -> currentModel.update { new } }
        val effects = triggeredEffects.takeIf { it.isNotEmpty() } ?: return@coroutineScope
        launch(effectContext) {
            effects
                .map { effect -> async { effect.run { trigger(dependency) } } }
                .awaitAll()
        }
    }
}

typealias AnyLoop = Loop<*, *, *, *, *>
