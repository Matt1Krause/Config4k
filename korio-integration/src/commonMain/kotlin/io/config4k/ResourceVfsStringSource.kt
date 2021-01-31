package io.config4k

import com.soywiz.korio.file.std.resourcesVfs
import io.config4k.async.AsyncStringSource

class ResourceVfsStringSource(private val path: String): AsyncStringSource {
    override suspend fun get(): String {
        //return resourcesVfs[path].readString()
        return ""
    }
}