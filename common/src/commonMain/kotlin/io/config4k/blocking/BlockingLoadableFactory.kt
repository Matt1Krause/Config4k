package io.config4k.blocking

import io.config4k.PropertyName
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

interface BlockingLoadableFactory<T> {
    fun load(loader: PropertyLoader, at: PropertyName): T
}

interface BlockingComplexLoadableFactory<T>: BlockingLoadableFactory<T> {
    @Deprecated(message = "do not override this method", replaceWith = ReplaceWith("load(PropertyLoader)"), DeprecationLevel.HIDDEN)
    override fun load(loader: PropertyLoader, at: PropertyName): T = load(loader)

    fun load(loader: PropertyLoader): T
}