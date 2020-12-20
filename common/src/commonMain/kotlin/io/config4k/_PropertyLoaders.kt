@file:Suppress("NOTHING_TO_INLINE")

package io.config4k

import io.config4k.async.*
import io.config4k.async.BaseNamedAsyncPropertyLoader
import io.config4k.blocking.*

suspend fun AsyncPropertyLoader.asyncNamed(name: PropertyName): AsyncPropertyLoader {
    return when (this) {
        is StructuredAsyncPropertyLoader -> advance(name)
        is AsyncStructuredPropertyLoader -> asyncAdvance(name)
        else -> BaseNamedAsyncPropertyLoader.ImmediateNamedAsyncPropertyLoader(this, name)
    }
}

fun AsyncPropertyLoader.named(name: PropertyName): AsyncPropertyLoader {
    return when (this) {
        is StructuredAsyncPropertyLoader -> advance(name)
        else -> BaseNamedAsyncPropertyLoader.ImmediateNamedAsyncPropertyLoader(this, name)
    }
}

fun PropertyLoader.named(name: PropertyName): PropertyLoader {
    return when (this) {
        is StructuredPropertyLoader -> advance(name)
        else -> io.config4k.blocking.BaseNamedPropertyLoader.ImmediateNamedPropertyLoader(this, name)
    }
}

suspend inline fun <T> AsyncPropertyLoader.asyncNamed(name: PropertyName, block: AsyncPropertyLoader.()->T): T {
    return asyncNamed(name).block()
}

inline fun <T> AsyncPropertyLoader.named(name: PropertyName, block: AsyncPropertyLoader.() -> T): T {
    return named(name).block()
}

inline fun <T> PropertyLoader.named(name: PropertyName, block: PropertyLoader.()->T): T {
    return named(name).block()
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