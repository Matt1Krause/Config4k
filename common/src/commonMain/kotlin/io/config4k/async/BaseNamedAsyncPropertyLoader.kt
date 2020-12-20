package io.config4k.async

import io.config4k.*

internal sealed class BaseNamedAsyncPropertyLoader(
    private val delegate: AsyncPropertyLoader,
    private val names: List<PropertyName>,
    private val precomputedNames: List<NameCaseContainer>,
    private val propertyPath: PropertyPath = MultiElementPropertyPath(names, precomputedNames)
): StructuredAsyncPropertyLoader {
    override fun advance(name: PropertyName): AsyncPropertyLoader {
        return NestedNamedAsyncPropertyLoader(this, name)
    }

    override suspend fun doesNotHave(propertyPath: PropertyPath, pathEnd: PropertyName): Boolean =
        delegate.doesNotHave(this.propertyPath + propertyPath, pathEnd)

    override suspend fun doesNotHave(pathEnd: PropertyName): Boolean = delegate.doesNotHave(propertyPath, pathEnd)

    override suspend fun loadByte(propertyPath: PropertyPath, pathEnd: PropertyName): Byte =
        delegate.loadByte(this.propertyPath + propertyPath, pathEnd)

    override suspend fun loadShort(propertyPath: PropertyPath, pathEnd: PropertyName): Short =
        delegate.loadShort(this.propertyPath + propertyPath, pathEnd)

    override suspend fun loadInt(propertyPath: PropertyPath, pathEnd: PropertyName): Int =
        delegate.loadInt(this.propertyPath + propertyPath, pathEnd)

    override suspend fun loadLong(propertyPath: PropertyPath, pathEnd: PropertyName): Long =
        delegate.loadLong(this.propertyPath + propertyPath, pathEnd)

    override suspend fun loadFloat(propertyPath: PropertyPath, pathEnd: PropertyName): Float =
        delegate.loadFloat(this.propertyPath + propertyPath, pathEnd)

    override suspend fun loadDouble(propertyPath: PropertyPath, pathEnd: PropertyName): Double =
        delegate.loadDouble(this.propertyPath + propertyPath, pathEnd)

    @ExperimentalUnsignedTypes
    override suspend fun loadUByte(propertyPath: PropertyPath, pathEnd: PropertyName): UByte =
        delegate.loadUByte(this.propertyPath + propertyPath, pathEnd)

    @ExperimentalUnsignedTypes
    override suspend fun loadUShort(propertyPath: PropertyPath, pathEnd: PropertyName): UShort =
        delegate.loadUShort(this.propertyPath + propertyPath, pathEnd)

    @ExperimentalUnsignedTypes
    override suspend fun loadUInt(propertyPath: PropertyPath, pathEnd: PropertyName): UInt =
        delegate.loadUInt(this.propertyPath + propertyPath, pathEnd)

    @ExperimentalUnsignedTypes
    override suspend fun loadULong(propertyPath: PropertyPath, pathEnd: PropertyName): ULong =
        delegate.loadULong(this.propertyPath + propertyPath, pathEnd)

    override suspend fun loadBoolean(propertyPath: PropertyPath, pathEnd: PropertyName): Boolean =
        delegate.loadBoolean(this.propertyPath + propertyPath, pathEnd)

    override suspend fun loadString(propertyPath: PropertyPath, pathEnd: PropertyName): String =
        delegate.loadString(this.propertyPath + propertyPath, pathEnd)

    override suspend fun loadByteArray(propertyPath: PropertyPath, pathEnd: PropertyName): ByteArray =
        delegate.loadByteArray(this.propertyPath + propertyPath, pathEnd)

    override suspend fun loadShortArray(propertyPath: PropertyPath, pathEnd: PropertyName): ShortArray =
        delegate.loadShortArray(this.propertyPath + propertyPath, pathEnd)

    override suspend fun loadIntArray(propertyPath: PropertyPath, pathEnd: PropertyName): IntArray =
        delegate.loadIntArray(this.propertyPath + propertyPath, pathEnd)

    override suspend fun loadLongArray(propertyPath: PropertyPath, pathEnd: PropertyName): LongArray =
        delegate.loadLongArray(this.propertyPath + propertyPath, pathEnd)

    override suspend fun loadFloatArray(propertyPath: PropertyPath, pathEnd: PropertyName): FloatArray =
        delegate.loadFloatArray(this.propertyPath + propertyPath, pathEnd)

    override suspend fun loadDoubleArray(propertyPath: PropertyPath, pathEnd: PropertyName): DoubleArray =
        delegate.loadDoubleArray(this.propertyPath + propertyPath, pathEnd)

    @ExperimentalUnsignedTypes
    override suspend fun loadUByteArray(propertyPath: PropertyPath, pathEnd: PropertyName): UByteArray =
        delegate.loadUByteArray(this.propertyPath + propertyPath, pathEnd)

    @ExperimentalUnsignedTypes
    override suspend fun loadUShortArray(propertyPath: PropertyPath, pathEnd: PropertyName): UShortArray =
        delegate.loadUShortArray(this.propertyPath + propertyPath, pathEnd)

    @ExperimentalUnsignedTypes
    override suspend fun loadUIntArray(propertyPath: PropertyPath, pathEnd: PropertyName): UIntArray =
        delegate.loadUIntArray(this.propertyPath + propertyPath, pathEnd)

    @ExperimentalUnsignedTypes
    override suspend fun loadULongArray(propertyPath: PropertyPath, pathEnd: PropertyName): ULongArray =
        delegate.loadULongArray(this.propertyPath + propertyPath, pathEnd)

    override suspend fun loadBooleanArray(propertyPath: PropertyPath, pathEnd: PropertyName): BooleanArray =
        delegate.loadBooleanArray(this.propertyPath + propertyPath, pathEnd)


    override suspend fun loadStringArray(propertyPath: PropertyPath, pathEnd: PropertyName): Array<String> =
        delegate.loadStringArray(this.propertyPath + propertyPath, pathEnd)

    override suspend fun loadByte(pathEnd: PropertyName): Byte =
        delegate.loadByte(propertyPath, pathEnd)

    override suspend fun loadShort(pathEnd: PropertyName): Short =
        delegate.loadShort(propertyPath, pathEnd)

    override suspend fun loadInt(pathEnd: PropertyName): Int =
        delegate.loadInt(propertyPath, pathEnd)

    override suspend fun loadLong(pathEnd: PropertyName): Long =
        delegate.loadLong(propertyPath, pathEnd)

    override suspend fun loadFloat(pathEnd: PropertyName): Float =
        delegate.loadFloat(propertyPath, pathEnd)

    override suspend fun loadDouble(pathEnd: PropertyName): Double =
        delegate.loadDouble(propertyPath, pathEnd)

    @ExperimentalUnsignedTypes
    override suspend fun loadUByte(pathEnd: PropertyName): UByte =
        delegate.loadUByte(propertyPath, pathEnd)

    @ExperimentalUnsignedTypes
    override suspend fun loadUShort(pathEnd: PropertyName): UShort =
        delegate.loadUShort(propertyPath, pathEnd)

    @ExperimentalUnsignedTypes
    override suspend fun loadUInt(pathEnd: PropertyName): UInt =
        delegate.loadUInt(propertyPath, pathEnd)

    @ExperimentalUnsignedTypes
    override suspend fun loadULong(pathEnd: PropertyName): ULong =
        delegate.loadULong(propertyPath, pathEnd)

    override suspend fun loadBoolean(pathEnd: PropertyName): Boolean =
        delegate.loadBoolean(propertyPath, pathEnd)

    override suspend fun loadString(pathEnd: PropertyName): String =
        delegate.loadString(propertyPath, pathEnd)

    override suspend fun loadByteArray(pathEnd: PropertyName): ByteArray =
        delegate.loadByteArray(propertyPath, pathEnd)

    override suspend fun loadShortArray(pathEnd: PropertyName): ShortArray =
        delegate.loadShortArray(propertyPath, pathEnd)

    override suspend fun loadIntArray(pathEnd: PropertyName): IntArray =
        delegate.loadIntArray(propertyPath, pathEnd)

    override suspend fun loadLongArray(pathEnd: PropertyName): LongArray =
        delegate.loadLongArray(propertyPath, pathEnd)

    override suspend fun loadFloatArray(pathEnd: PropertyName): FloatArray =
        delegate.loadFloatArray(propertyPath, pathEnd)

    override suspend fun loadDoubleArray(pathEnd: PropertyName): DoubleArray =
        delegate.loadDoubleArray(propertyPath, pathEnd)

    @ExperimentalUnsignedTypes
    override suspend fun loadUByteArray(pathEnd: PropertyName): UByteArray =
        delegate.loadUByteArray(propertyPath, pathEnd)

    @ExperimentalUnsignedTypes
    override suspend fun loadUShortArray(pathEnd: PropertyName): UShortArray =
        delegate.loadUShortArray(propertyPath, pathEnd)

    @ExperimentalUnsignedTypes
    override suspend fun loadUIntArray(pathEnd: PropertyName): UIntArray =
        delegate.loadUIntArray(propertyPath, pathEnd)

    @ExperimentalUnsignedTypes
    override suspend fun loadULongArray(pathEnd: PropertyName): ULongArray =
        delegate.loadULongArray(propertyPath, pathEnd)

    override suspend fun loadBooleanArray(pathEnd: PropertyName): BooleanArray =
        delegate.loadBooleanArray(propertyPath, pathEnd)

    override suspend fun loadStringArray(pathEnd: PropertyName): Array<String> =
        delegate.loadStringArray(propertyPath, pathEnd)

    internal class ImmediateNamedAsyncPropertyLoader(delegate: AsyncPropertyLoader, name: PropertyName):
        BaseNamedAsyncPropertyLoader(delegate, listOf(name), listOf(NameCaseContainer(name)), SingleElementPropertyPath(name))
    internal class NestedNamedAsyncPropertyLoader(delegate: BaseNamedAsyncPropertyLoader, name: PropertyName):
        BaseNamedAsyncPropertyLoader(delegate.delegate, delegate.names.plusElement(name), delegate.precomputedNames + NameCaseContainer(name))
}