package io.config4k.async

import io.config4k.PropertyName

interface AsyncStructuredPropertyLoader: AsyncPropertyLoader {
    suspend fun asyncAdvance(name: PropertyName): AsyncPropertyLoader
}

interface StructuredAsyncPropertyLoader: AsyncStructuredPropertyLoader {
    override suspend fun asyncAdvance(name: PropertyName): AsyncPropertyLoader = advance(name)
    fun advance(name: PropertyName): AsyncPropertyLoader
}