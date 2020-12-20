package io.config4k.jvm

import io.config4k.blocking.StringSource
import java.nio.file.Files
import java.nio.file.Path

class PathStringSource(private val path: Path): StringSource {
    override fun get(): String {
        //TODO -> Async Variant
        return InputStreamStringSource(Files.newInputStream(path)).get()
    }
}