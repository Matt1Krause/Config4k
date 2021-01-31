package io.config4k.annotation

import kotlin.reflect.KClass

annotation class Transform(val qualifiedName: String, val function: Boolean = false) {
    companion object
}
annotation class TransformWith(val transformerClass: KClass<*>)
