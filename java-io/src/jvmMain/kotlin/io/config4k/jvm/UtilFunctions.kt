@file:Suppress("NOTHING_TO_INLINE")

package io.config4k.jvm

import io.config4k.async.AsyncPropertyLoader
import io.config4k.async.StringAsyncLoaderFactory
import io.config4k.blocking.PropertyLoader
import io.config4k.blocking.StringLoaderFactory
import io.config4k.blocking.StringSource
import io.config4k.from
import java.io.File
import java.io.InputStream
import java.nio.file.Path

inline fun File.stringSource(): StringSource = FileStringSource(this)
inline fun InputStream.stringSource(): StringSource = InputStreamStringSource(this)
inline fun Path.stringSource(): StringSource = PathStringSource(this)

inline infix fun StringAsyncLoaderFactory.from(file: File): AsyncPropertyLoader = this from file.stringSource()
inline infix fun StringLoaderFactory.from(file: File): PropertyLoader = this from file.stringSource()