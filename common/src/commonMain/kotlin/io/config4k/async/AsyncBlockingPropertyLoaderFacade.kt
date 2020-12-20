package io.config4k.async

import io.config4k.PropertyName
import io.config4k.PropertyPath
import io.config4k.async
import io.config4k.blocking.PropertyLoader
import io.config4k.blocking.StructuredPropertyLoader

sealed class AsyncBlockingPropertyLoaderFacade(protected val delegate: PropertyLoader): AsyncPropertyLoader {
    class AsyncBlockingPropertyLoaderFacadeImpl(delegate: PropertyLoader): AsyncBlockingPropertyLoaderFacade(delegate)
    class StructuredAsyncBlockingPropertyLoaderFacade(delegate: StructuredPropertyLoader) :
        AsyncBlockingPropertyLoaderFacade(delegate), StructuredAsyncPropertyLoader {
        override fun advance(name: PropertyName): AsyncPropertyLoader {
            return (delegate as StructuredPropertyLoader).advance(name).async()
        }
    }

    override suspend fun doesNotHave(propertyPath: PropertyPath, pathEnd: PropertyName): Boolean {
        return delegate.doesNotHave(propertyPath, pathEnd)
    }

    override suspend fun doesNotHave(pathEnd: PropertyName): Boolean {
        return delegate.doesNotHave(pathEnd)
    }

    override suspend fun loadByte(propertyPath: PropertyPath, pathEnd: PropertyName): Byte {
        return delegate.loadByte(propertyPath, pathEnd)
    }

    override suspend fun loadShort(propertyPath: PropertyPath, pathEnd: PropertyName): Short {
        return delegate.loadShort(propertyPath, pathEnd)
    }

    override suspend fun loadInt(propertyPath: PropertyPath, pathEnd: PropertyName): Int {
        return delegate.loadInt(propertyPath, pathEnd)
    }

    override suspend fun loadLong(propertyPath: PropertyPath, pathEnd: PropertyName): Long {
        return delegate.loadLong(propertyPath, pathEnd)
    }

    override suspend fun loadFloat(propertyPath: PropertyPath, pathEnd: PropertyName): Float {
        return delegate.loadFloat(propertyPath, pathEnd)
    }

    override suspend fun loadDouble(propertyPath: PropertyPath, pathEnd: PropertyName): Double {
        return delegate.loadDouble(propertyPath, pathEnd)
    }

    @ExperimentalUnsignedTypes
    override suspend fun loadUByte(propertyPath: PropertyPath, pathEnd: PropertyName): UByte {
        return delegate.loadUByte(propertyPath, pathEnd)
    }

    @ExperimentalUnsignedTypes
    override suspend fun loadUShort(propertyPath: PropertyPath, pathEnd: PropertyName): UShort {
        return delegate.loadUShort(propertyPath, pathEnd)
    }

    @ExperimentalUnsignedTypes
    override suspend fun loadUInt(propertyPath: PropertyPath, pathEnd: PropertyName): UInt {
        return delegate.loadUInt(propertyPath, pathEnd)
    }

    @ExperimentalUnsignedTypes
    override suspend fun loadULong(propertyPath: PropertyPath, pathEnd: PropertyName): ULong {
        return delegate.loadULong(propertyPath, pathEnd)
    }

    override suspend fun loadBoolean(propertyPath: PropertyPath, pathEnd: PropertyName): Boolean {
        return delegate.loadBoolean(propertyPath, pathEnd)
    }

    override suspend fun loadString(propertyPath: PropertyPath, pathEnd: PropertyName): String {
        return delegate.loadString(propertyPath, pathEnd)
    }

    override suspend fun loadByteArray(propertyPath: PropertyPath, pathEnd: PropertyName): ByteArray {
        return delegate.loadByteArray(propertyPath, pathEnd)
    }

    override suspend fun loadShortArray(propertyPath: PropertyPath, pathEnd: PropertyName): ShortArray {
        return delegate.loadShortArray(propertyPath, pathEnd)
    }

    override suspend fun loadIntArray(propertyPath: PropertyPath, pathEnd: PropertyName): IntArray {
        return delegate.loadIntArray(propertyPath, pathEnd)
    }

    override suspend fun loadLongArray(propertyPath: PropertyPath, pathEnd: PropertyName): LongArray {
        return delegate.loadLongArray(propertyPath, pathEnd)
    }

    override suspend fun loadFloatArray(propertyPath: PropertyPath, pathEnd: PropertyName): FloatArray {
        return delegate.loadFloatArray(propertyPath, pathEnd)
    }

    override suspend fun loadDoubleArray(propertyPath: PropertyPath, pathEnd: PropertyName): DoubleArray {
        return delegate.loadDoubleArray(propertyPath, pathEnd)
    }

    @ExperimentalUnsignedTypes
    override suspend fun loadUByteArray(propertyPath: PropertyPath, pathEnd: PropertyName): UByteArray {
        return delegate.loadUByteArray(propertyPath, pathEnd)
    }

    @ExperimentalUnsignedTypes
    override suspend fun loadUShortArray(propertyPath: PropertyPath, pathEnd: PropertyName): UShortArray {
        return delegate.loadUShortArray(propertyPath, pathEnd)
    }

    @ExperimentalUnsignedTypes
    override suspend fun loadUIntArray(propertyPath: PropertyPath, pathEnd: PropertyName): UIntArray {
        return delegate.loadUIntArray(propertyPath, pathEnd)
    }

    @ExperimentalUnsignedTypes
    override suspend fun loadULongArray(propertyPath: PropertyPath, pathEnd: PropertyName): ULongArray {
        return delegate.loadULongArray(propertyPath, pathEnd)
    }

    override suspend fun loadBooleanArray(propertyPath: PropertyPath, pathEnd: PropertyName): BooleanArray {
        return delegate.loadBooleanArray(propertyPath, pathEnd)
    }

    override suspend fun loadStringArray(propertyPath: PropertyPath, pathEnd: PropertyName): Array<String> {
        return delegate.loadStringArray(propertyPath, pathEnd)
    }

    override suspend fun loadByte(pathEnd: PropertyName): Byte {
        return delegate.loadByte(pathEnd)
    }

    override suspend fun loadShort(pathEnd: PropertyName): Short {
        return delegate.loadShort(pathEnd)
    }

    override suspend fun loadInt(pathEnd: PropertyName): Int {
        return delegate.loadInt(pathEnd)
    }

    override suspend fun loadLong(pathEnd: PropertyName): Long {
        return delegate.loadLong(pathEnd)
    }

    override suspend fun loadFloat(pathEnd: PropertyName): Float {
        return delegate.loadFloat(pathEnd)
    }

    override suspend fun loadDouble(pathEnd: PropertyName): Double {
        return delegate.loadDouble(pathEnd)
    }

    @ExperimentalUnsignedTypes
    override suspend fun loadUByte(pathEnd: PropertyName): UByte {
        return delegate.loadUByte(pathEnd)
    }

    @ExperimentalUnsignedTypes
    override suspend fun loadUShort(pathEnd: PropertyName): UShort {
        return delegate.loadUShort(pathEnd)
    }

    @ExperimentalUnsignedTypes
    override suspend fun loadUInt(pathEnd: PropertyName): UInt {
        return delegate.loadUInt(pathEnd)
    }

    @ExperimentalUnsignedTypes
    override suspend fun loadULong(pathEnd: PropertyName): ULong {
        return delegate.loadULong(pathEnd)
    }

    override suspend fun loadBoolean(pathEnd: PropertyName): Boolean {
        return delegate.loadBoolean(pathEnd)
    }

    override suspend fun loadString(pathEnd: PropertyName): String {
        return delegate.loadString(pathEnd)
    }

    override suspend fun loadByteArray(pathEnd: PropertyName): ByteArray {
        return delegate.loadByteArray(pathEnd)
    }

    override suspend fun loadShortArray(pathEnd: PropertyName): ShortArray {
        return delegate.loadShortArray(pathEnd)
    }

    override suspend fun loadIntArray(pathEnd: PropertyName): IntArray {
        return delegate.loadIntArray(pathEnd)
    }

    override suspend fun loadLongArray(pathEnd: PropertyName): LongArray {
        return delegate.loadLongArray(pathEnd)
    }

    override suspend fun loadFloatArray(pathEnd: PropertyName): FloatArray {
        return delegate.loadFloatArray(pathEnd)
    }

    override suspend fun loadDoubleArray(pathEnd: PropertyName): DoubleArray {
        return delegate.loadDoubleArray(pathEnd)
    }

    @ExperimentalUnsignedTypes
    override suspend fun loadUByteArray(pathEnd: PropertyName): UByteArray {
        return delegate.loadUByteArray(pathEnd)
    }

    @ExperimentalUnsignedTypes
    override suspend fun loadUShortArray(pathEnd: PropertyName): UShortArray {
        return delegate.loadUShortArray(pathEnd)
    }

    @ExperimentalUnsignedTypes
    override suspend fun loadUIntArray(pathEnd: PropertyName): UIntArray {
        return delegate.loadUIntArray(pathEnd)
    }

    @ExperimentalUnsignedTypes
    override suspend fun loadULongArray(pathEnd: PropertyName): ULongArray {
        return delegate.loadULongArray(pathEnd)
    }

    override suspend fun loadBooleanArray(pathEnd: PropertyName): BooleanArray {
        return delegate.loadBooleanArray(pathEnd)
    }

    override suspend fun loadStringArray(pathEnd: PropertyName): Array<String> {
        return delegate.loadStringArray(pathEnd)
    }
}