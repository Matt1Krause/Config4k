package io.config4k.jvm

import io.config4k.blocking.StringSource
import java.io.InputStream
import java.nio.charset.Charset

class InputStreamStringSource(private val inputStream: InputStream, private val charset: Charset = Charset.forName("utf8")):
    StringSource {
    override fun get(): String {
        return inputStream.use { it.readAllBytes().toString(charset) }
    }
}