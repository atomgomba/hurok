package com.ekezet.hurok.test

import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.FIELD
import kotlin.annotation.AnnotationTarget.FILE
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.annotation.AnnotationTarget.PROPERTY
import kotlin.annotation.AnnotationTarget.PROPERTY_GETTER
import kotlin.annotation.AnnotationTarget.PROPERTY_SETTER

@Retention(RUNTIME)
@Target(FILE, CLASS, FIELD, FUNCTION, PROPERTY, PROPERTY_GETTER, PROPERTY_SETTER)
annotation class CoverageIgnore
