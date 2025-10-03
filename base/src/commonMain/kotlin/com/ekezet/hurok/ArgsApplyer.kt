package com.ekezet.hurok

fun interface ArgsApplyer<TModel : Any, in TArgs> {
    fun TModel.applyArgs(args: TArgs & Any): TModel
}