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
    private val dependency: TDependency? = null,
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

    val state: Flow<TState> = currentModel
        .map(renderer::renderState)
        .distinctUntilChanged()

    private val actions = MutableSharedFlow<TAction>()

    private lateinit var _scope: CoroutineScope
    override val scope: CoroutineScope
        @Throws(IllegalArgumentException::class)
        get() = if (!::_scope.isInitialized) {
            error("Loop must be started. Please call startIn() first!")
        } else {
            _scope
        }

    final override fun emit(action: Action<TModel, TDependency>) {
        scope.launch {
            @Suppress("UNCHECKED_CAST") actions.emit(action as TAction)
        }
    }

    final override fun addChildEmitter(child: AnyActionEmitter) {
        requireNotNull(dependency as? DependencyContainer) {
            "Dependency must implement DependencyContainer before adding a child emitter"
        }.plus(child)
    }

    /**
     * Apply new arguments to this loop.
     *
     * @param args new arguments
     * @return the loop
     * @throws IllegalStateException if [argsApplyer] is null
     */
    fun applyArgs(args: TArgs & Any): Loop<TState, TModel, TArgs, TDependency, TAction> = apply {
        requireNotNull(argsApplyer) { "argsApplyer must not be null" }
        currentModel.update { model ->
            with(argsApplyer) { model.applyArgs(args) }
        }
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
