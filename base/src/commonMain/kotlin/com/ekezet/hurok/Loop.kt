package com.ekezet.hurok

import com.ekezet.hurok.test.CoverageIgnore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * Base class that handles unidirectional data flow.
 *
 * @param model model to inject
 * @param renderer transform model into state
 * @param args input arguments
 * @param firstAction action to emit when loop is constructed
 * @param dependency dependency container
 * @param effectContext dispatcher for launching effects
 */
abstract class Loop<out TState : ViewState<TModel, TDependency>, TModel : Any, in TArgs, TDependency, in TAction : Action<TModel, TDependency>>(
    model: TModel,
    renderer: Renderer<TModel, TDependency, TState>,
    args: TArgs? = null,
    private val firstAction: TAction? = null,
    private val dependency: TDependency? = null,
    private val effectContext: CoroutineContext = DispatcherProvider.IO,
) : ActionEmitter<TModel, TDependency> {

    private val currentModel = MutableStateFlow(model.run {
        args?.let { applyArgs(it) } ?: this
    })

    val state: Flow<TState> = currentModel.map(renderer::renderState).onEach {
        it.emitter = this
    }

    private val actions = MutableSharedFlow<TAction>()

    private lateinit var _parentScope: CoroutineScope
    override val parentScope: CoroutineScope
        get() = if (!::_parentScope.isInitialized) {
            error("Loop must be started")
        } else {
            _parentScope
        }

    final override fun emit(action: Action<TModel, TDependency>) {
        parentScope.launch {
            @Suppress("UNCHECKED_CAST") actions.emit(action as TAction)
        }
    }

    final override fun <TChildModel : Any, TChildDependency> addChildEmitter(
        child: ActionEmitter<TChildModel, TChildDependency>,
    ) {
        requireNotNull(dependency) {
            "Dependency must be set before adding a child emitter"
        }.onAddChildEmitter(child)
    }

    fun applyArgs(args: TArgs) {
        currentModel.update { currentModel.value.applyArgs(args) }
    }

    fun startIn(parentScope: CoroutineScope): Loop<TState, TModel, TArgs, TDependency, TAction> {
        _parentScope = parentScope
        parentScope.launch { actions.collect(::onNextAction) }
        firstAction?.let { emit(it) }
        return this
    }

    @CoverageIgnore
    protected open fun TDependency.onAddChildEmitter(child: AnyActionEmitter) {
        throw NotImplementedError("This method must be overridden to add child loops")
    }

    @CoverageIgnore
    protected open fun TModel.applyArgs(args: TArgs): TModel {
        throw NotImplementedError("This method must be overridden to apply arguments")
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
