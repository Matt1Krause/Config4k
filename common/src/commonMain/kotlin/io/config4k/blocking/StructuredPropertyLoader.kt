package io.config4k.blocking

import io.config4k.PropertyName

interface StructuredPropertyLoader: PropertyLoader {
    fun advance(name: PropertyName): PropertyLoader
}