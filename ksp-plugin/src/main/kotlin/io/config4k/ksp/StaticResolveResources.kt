package io.config4k.ksp

import io.config4k.async.AsyncPropertyLoader

class StaticResolveResources {
    val loaders: MutableMap<String, AsyncPropertyLoader> = HashMap()
}