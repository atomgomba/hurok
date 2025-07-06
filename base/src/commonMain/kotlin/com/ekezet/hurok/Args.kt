package com.ekezet.hurok

/**
 * Runtime arguments for a [Loop].
 *
 * @param TModel
 */
interface Args<TModel : Any> {
    fun applyToModel(model: TModel): TModel
}
