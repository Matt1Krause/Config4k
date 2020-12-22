package io.config4k.jvm

import io.config4k.blocking.StringSource
import java.io.File

class FileStringSource(private val file: File): StringSource {
    override fun get(): String {
        return InputStreamStringSource(file.inputStream()).get()
    }
}