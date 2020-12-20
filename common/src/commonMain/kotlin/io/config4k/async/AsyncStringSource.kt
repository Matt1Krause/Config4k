package io.config4k.async

interface AsyncStringSource {
    suspend fun get(): String
}