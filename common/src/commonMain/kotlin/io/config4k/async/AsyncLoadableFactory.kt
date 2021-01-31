package io.config4k.async

import io.config4k.PropertyName
import kotlin.jvm.JvmSynthetic

interface AsyncLoadableFactory<T> {
    suspend fun load(loader: AsyncPropertyLoader, at: PropertyName): T
}

interface AsyncComplexLoadableFactory<T>: AsyncLoadableFactory<T> {
    @Deprecated(message = "do not override this method", replaceWith = ReplaceWith("load(PropertyLoader)"), DeprecationLevel.HIDDEN)
    override suspend fun load(loader: AsyncPropertyLoader, at: PropertyName): T = load(loader)
    suspend fun load(loader: AsyncPropertyLoader): T
}