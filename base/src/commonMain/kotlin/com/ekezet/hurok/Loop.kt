package com.ekezet.hurok

import com.ekezet.hurok.utils.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * Base class that handles unidirectional data flow.
 *
 * @param model the initial model
 * @param renderer transform model into state
 * @param args input arguments
 * @param onStart block to run when the loop starts
 * @param dependency dependency container
 * @param effectContext dispatcher for launching effects
 */
abstract class Loop<out TState : ViewState<TModel, TDependency>, TModel : Any, in TArgs, TDependency, in TAction : Action<TModel, TDependency>>(
    model: TModel,
    renderer: Renderer<TState, TModel>,
    args: TArgs? = null,
    private val argsApplyer: ArgsApplyer<TModel, TArgs>? = null,
    private val onStart: ActionEmitter<TModel, TDependency>.() -> Unit = {},
    private val dependency: TDependency? = null,
    private val effectContext: CoroutineContext = Dispatchers.IO,
) : ActionEmitter<TModel, TDependency> {

    private val currentModel = MutableStateFlow(model.run {
        if (argsApplyer == null || args == null) {
            this
        } else {
            with(argsApplyer) { applyArgs(args) }
        }
    })

    val state: Flow<TState> = currentModel
        .map(renderer::renderState)
        .distinctUntilChanged()
        .onEach {
            it.emitter = this
        }

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

    fun applyArgs(args: TArgs & Any) {
        requireNotNull(argsApplyer) { "argsApplyer must not be null" }
        currentModel.update { model ->
            with(argsApplyer) {
                model.applyArgs(args)
            }
        }
    }

    /**
     * Start the loop in the given scope.
     *
     * @param scope the scope used to launch actions
     * @throws IllegalStateException if the loop has already started
     */
    @Throws(IllegalArgumentException::class)
    fun startIn(scope: CoroutineScope): Loop<TState, TModel, TArgs, TDependency, TAction> {
        _scope = scope
        scope.launch { actions.collect(::onNextAction) }
        onStart()
        return this
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

