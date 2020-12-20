package io.config4k.blocking

interface StringLoaderFactory {
    fun get(string: String): PropertyLoader
}