package io.config4k.async

interface StringAsyncLoaderFactory {
    fun get(string: String): AsyncPropertyLoader
}