package com.ekezet.hurok

/**
 * Can be used to attach child emitters when implemented by a [Loop] dependency.
 */
interface HasActionEmitter {
    /**
     * Add a child emitter to this dependency.
     */
    operator fun plus(other: AnyActionEmitter)
}
