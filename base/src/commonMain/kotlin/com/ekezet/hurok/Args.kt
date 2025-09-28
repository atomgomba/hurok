package com.ekezet.hurok

/**
 * Runtime arguments for a [Loop].
 *
 * @param TModel
 */
abstract class Args<TModel : Any> {
    internal abstract fun applyToModel(model: TModel): TModel
}
