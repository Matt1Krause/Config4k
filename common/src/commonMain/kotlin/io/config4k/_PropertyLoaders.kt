@file:Suppress("NOTHING_TO_INLINE")

package io.config4k

import io.config4k.async.*
import io.config4k.async.BaseNamedAsyncPropertyLoader
import io.config4k.blocking.*

suspend fun AsyncPropertyLoader.asyncNested(name: PropertyName): AsyncPropertyLoader {
    return when (this) {
        is StructuredAsyncPropertyLoader -> advance(name)
        is AsyncStructuredPropertyLoader -> asyncAdvance(name)
        else -> BaseNamedAsyncPropertyLoader.ImmediateNamedAsyncPropertyLoader(this, name)
    }
}

fun AsyncPropertyLoader.nested(name: PropertyName): AsyncPropertyLoader {
    return when (this) {
        is StructuredAsyncPropertyLoader -> advance(name)
        else -> BaseNamedAsyncPropertyLoader.ImmediateNamedAsyncPropertyLoader(this, name)
    }
}

fun PropertyLoader.nested(name: PropertyName): PropertyLoader {
    return when (this) {
        is StructuredPropertyLoader -> advance(name)
        else -> io.config4k.blocking.BaseNamedPropertyLoader.ImmediateNamedPropertyLoader(this, name)
    }
}

suspend inline fun <T> AsyncPropertyLoader.asyncNested(name: PropertyName, block: AsyncPropertyLoader.()->T): T {
    return asyncNested(name).block()
}

inline fun <T> AsyncPropertyLoader.nested(name: PropertyName, block: AsyncPropertyLoader.() -> T): T {
    return nested(name).block()
}

inline fun <T> PropertyLoader.nested(name: PropertyName, block: PropertyLoader.()->T): T {
    return nested(name).block()
}

suspend inline infix fun StringAsyncLoaderFactory.from(source: AsyncStringSource): AsyncPropertyLoader = get(source.get())

inline infix fun StringAsyncLoaderFactory.from(source: StringSource): AsyncPropertyLoader = get(source.get())

suspend inline infix fun StringLoaderFactory.from(source: AsyncStringSource): PropertyLoader = get(source.get())

inline infix fun StringLoaderFactory.from(source: StringSource): PropertyLoader = get(source.get())

inline fun PropertyLoader.async() =
    if (this is StructuredPropertyLoader)
        AsyncBlockingPropertyLoaderFacade.StructuredAsyncBlockingPropertyLoaderFacade(this)
    else
        AsyncBlockingPropertyLoaderFacade.AsyncBlockingPropertyLoaderFacadeImpl(this)

inline fun <T> AsyncPropertyLoader.getOrDefault(default: ()->T, get: AsyncPropertyLoader.()-> T): T {
    return try {
        get()
    } catch (e: InvalidPropertyException) {
        default()
    }
}

inline fun <T> PropertyLoader.getOrDefault(default: ()->T, get: PropertyLoader.()-> T): T {
    return try {
        get()
    } catch (e: InvalidPropertyException) {
        default()
    }
}