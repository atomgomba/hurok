package com.ekezet.hurok

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * Base class that handles unidirectional data flow.
 *
 * @param model model to inject
 * @param args input arguments
 * @param dependency dependency container
 * @param effectDispatcher dispatcher for launching effects
 */
abstract class Loop<out TState : ViewState<TModel, TDependency>, TModel : Any, in TArgs, TDependency, in TAction : Action<TModel, TDependency>>(
    model: TModel,
    renderer: Renderer<TModel, TDependency, TState>,
    args: TArgs? = null,
    private val firstAction: TAction? = null,
    private val dependency: TDependency? = null,
    private val effectDispatcher: CoroutineDispatcher = DispatcherProvider.IO,
) : ActionEmitter<TModel, TDependency> {

    private val _model = MutableStateFlow(model.run {
        args?.let { applyArgs(it) } ?: this
    })

    val state: Flow<TState> = _model.map(renderer::renderState).onEach {
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
        _model.value = _model.value.applyArgs(args)
    }

    fun startIn(parentScope: CoroutineScope): Loop<TState, TModel, TArgs, TDependency, TAction> {
        _parentScope = parentScope
        parentScope.launch { actions.collect(::onNextAction) }
        firstAction?.let { emit(it) }
        return this
    }

    protected open fun TDependency.onAddChildEmitter(child: AnyActionEmitter) {
        throw NotImplementedError("This method must be overridden to add child loops")
    }

    protected open fun TModel.applyArgs(args: TArgs): TModel {
        throw NotImplementedError("This method must be overridden to apply arguments")
    }

    private fun onNextAction(action: TAction) = parentScope.launch {
        val (updatedModel, triggeredEffects) = action.run { _model.value.proceed() }
        updatedModel?.let { update -> _model.value = update }
        val effects = triggeredEffects.takeIf { it.isNotEmpty() } ?: return@launch
        launch(effectDispatcher) {
            val deferreds = effects.map { effect ->
                async { effect.run { trigger(dependency) } }
            }
            deferreds.awaitAll()
        }
    }
}
