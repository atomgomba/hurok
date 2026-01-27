package com.ekezet.hurok

/**
 * Applies arguments to a model.
 *
 * @param TModel The type of the model.
 * @param TArgs The type of the arguments.
 */
fun interface ArgsApplyer<TModel : Any, in TArgs> {
    /**
     * Applies the given arguments to this model.
     *
     * @param args The arguments to apply.
     * @return The model with the arguments applied.
     */
    fun TModel.applyArgs(args: TArgs & Any): TModel
}