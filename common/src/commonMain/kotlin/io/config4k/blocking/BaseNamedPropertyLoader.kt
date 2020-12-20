package io.config4k.blocking

import io.config4k.*

internal sealed class BaseNamedPropertyLoader(
    private val delegate: PropertyLoader,
    private val names: List<PropertyName>,
    private val precomputedNames: List<NameCaseContainer>,
    private val propertyPath: PropertyPath = MultiElementPropertyPath(names, precomputedNames)
) : StructuredPropertyLoader {

    override fun advance(name: PropertyName): PropertyLoader {
        return NestedNamedPropertyLoader(this, name)
    }

    override fun doesNotHave(propertyPath: PropertyPath, pathEnd: PropertyName): Boolean =
        delegate.doesNotHave(this.propertyPath + propertyPath, pathEnd)

    override fun doesNotHave(pathEnd: PropertyName): Boolean =
        delegate.doesNotHave(propertyPath, pathEnd)

    override fun loadByte(propertyPath: PropertyPath, pathEnd: PropertyName): Byte =
        delegate.loadByte(this.propertyPath + propertyPath, pathEnd)

    override fun loadShort(propertyPath: PropertyPath, pathEnd: PropertyName): Short =
        delegate.loadShort(this.propertyPath + propertyPath, pathEnd)

    override fun loadInt(propertyPath: PropertyPath, pathEnd: PropertyName): Int =
        delegate.loadInt(this.propertyPath + propertyPath, pathEnd)

    override fun loadLong(propertyPath: PropertyPath, pathEnd: PropertyName): Long =
        delegate.loadLong(this.propertyPath + propertyPath, pathEnd)

    override fun loadFloat(propertyPath: PropertyPath, pathEnd: PropertyName): Float =
        delegate.loadFloat(this.propertyPath + propertyPath, pathEnd)

    override fun loadDouble(propertyPath: PropertyPath, pathEnd: PropertyName): Double =
        delegate.loadDouble(this.propertyPath + propertyPath, pathEnd)

    @ExperimentalUnsignedTypes
    override fun loadUByte(propertyPath: PropertyPath, pathEnd: PropertyName): UByte =
        delegate.loadUByte(this.propertyPath + propertyPath, pathEnd)

    @ExperimentalUnsignedTypes
    override fun loadUShort(propertyPath: PropertyPath, pathEnd: PropertyName): UShort =
        delegate.loadUShort(this.propertyPath + propertyPath, pathEnd)

    @ExperimentalUnsignedTypes
    override fun loadUInt(propertyPath: PropertyPath, pathEnd: PropertyName): UInt =
        delegate.loadUInt(this.propertyPath + propertyPath, pathEnd)

    @ExperimentalUnsignedTypes
    override fun loadULong(propertyPath: PropertyPath, pathEnd: PropertyName): ULong =
        delegate.loadULong(this.propertyPath + propertyPath, pathEnd)

    override fun loadBoolean(propertyPath: PropertyPath, pathEnd: PropertyName): Boolean =
        delegate.loadBoolean(this.propertyPath + propertyPath, pathEnd)

    override fun loadString(propertyPath: PropertyPath, pathEnd: PropertyName): String =
        delegate.loadString(this.propertyPath + propertyPath, pathEnd)

    override fun loadByteArray(propertyPath: PropertyPath, pathEnd: PropertyName): ByteArray =
        delegate.loadByteArray(this.propertyPath + propertyPath, pathEnd)

    override fun loadShortArray(propertyPath: PropertyPath, pathEnd: PropertyName): ShortArray =
        delegate.loadShortArray(this.propertyPath + propertyPath, pathEnd)

    override fun loadIntArray(propertyPath: PropertyPath, pathEnd: PropertyName): IntArray =
        delegate.loadIntArray(this.propertyPath + propertyPath, pathEnd)

    override fun loadLongArray(propertyPath: PropertyPath, pathEnd: PropertyName): LongArray =
        delegate.loadLongArray(this.propertyPath + propertyPath, pathEnd)

    override fun loadFloatArray(propertyPath: PropertyPath, pathEnd: PropertyName): FloatArray =
        delegate.loadFloatArray(this.propertyPath + propertyPath, pathEnd)

    override fun loadDoubleArray(propertyPath: PropertyPath, pathEnd: PropertyName): DoubleArray =
        delegate.loadDoubleArray(this.propertyPath + propertyPath, pathEnd)

    @ExperimentalUnsignedTypes
    override fun loadUByteArray(propertyPath: PropertyPath, pathEnd: PropertyName): UByteArray =
        delegate.loadUByteArray(this.propertyPath + propertyPath, pathEnd)

    @ExperimentalUnsignedTypes
    override fun loadUShortArray(propertyPath: PropertyPath, pathEnd: PropertyName): UShortArray =
        delegate.loadUShortArray(this.propertyPath + propertyPath, pathEnd)

    @ExperimentalUnsignedTypes
    override fun loadUIntArray(propertyPath: PropertyPath, pathEnd: PropertyName): UIntArray =
        delegate.loadUIntArray(this.propertyPath + propertyPath, pathEnd)

    @ExperimentalUnsignedTypes
    override fun loadULongArray(propertyPath: PropertyPath, pathEnd: PropertyName): ULongArray =
        delegate.loadULongArray(this.propertyPath + propertyPath, pathEnd)

    override fun loadBooleanArray(propertyPath: PropertyPath, pathEnd: PropertyName): BooleanArray =
        delegate.loadBooleanArray(this.propertyPath + propertyPath, pathEnd)


    override fun loadStringArray(propertyPath: PropertyPath, pathEnd: PropertyName): Array<String> =
        delegate.loadStringArray(this.propertyPath + propertyPath, pathEnd)

    override fun loadByte(pathEnd: PropertyName): Byte =
        delegate.loadByte(propertyPath, pathEnd)

    override fun loadShort(pathEnd: PropertyName): Short =
        delegate.loadShort(propertyPath, pathEnd)

    override fun loadInt(pathEnd: PropertyName): Int =
        delegate.loadInt(propertyPath, pathEnd)

    override fun loadLong(pathEnd: PropertyName): Long =
        delegate.loadLong(propertyPath, pathEnd)

    override fun loadFloat(pathEnd: PropertyName): Float =
        delegate.loadFloat(propertyPath, pathEnd)

    override fun loadDouble(pathEnd: PropertyName): Double =
        delegate.loadDouble(propertyPath, pathEnd)

    @ExperimentalUnsignedTypes
    override fun loadUByte(pathEnd: PropertyName): UByte =
        delegate.loadUByte(propertyPath, pathEnd)

    @ExperimentalUnsignedTypes
    override fun loadUShort(pathEnd: PropertyName): UShort =
        delegate.loadUShort(propertyPath, pathEnd)

    @ExperimentalUnsignedTypes
    override fun loadUInt(pathEnd: PropertyName): UInt =
        delegate.loadUInt(propertyPath, pathEnd)

    @ExperimentalUnsignedTypes
    override fun loadULong(pathEnd: PropertyName): ULong =
        delegate.loadULong(propertyPath, pathEnd)

    override fun loadBoolean(pathEnd: PropertyName): Boolean =
        delegate.loadBoolean(propertyPath, pathEnd)

    override fun loadString(pathEnd: PropertyName): String =
        delegate.loadString(propertyPath, pathEnd)

    override fun loadByteArray(pathEnd: PropertyName): ByteArray =
        delegate.loadByteArray(propertyPath, pathEnd)

    override fun loadShortArray(pathEnd: PropertyName): ShortArray =
        delegate.loadShortArray(propertyPath, pathEnd)

    override fun loadIntArray(pathEnd: PropertyName): IntArray =
        delegate.loadIntArray(propertyPath, pathEnd)

    override fun loadLongArray(pathEnd: PropertyName): LongArray =
        delegate.loadLongArray(propertyPath, pathEnd)

    override fun loadFloatArray(pathEnd: PropertyName): FloatArray =
        delegate.loadFloatArray(propertyPath, pathEnd)

    override fun loadDoubleArray(pathEnd: PropertyName): DoubleArray =
        delegate.loadDoubleArray(propertyPath, pathEnd)

    @ExperimentalUnsignedTypes
    override fun loadUByteArray(pathEnd: PropertyName): UByteArray =
        delegate.loadUByteArray(propertyPath, pathEnd)

    @ExperimentalUnsignedTypes
    override fun loadUShortArray(pathEnd: PropertyName): UShortArray =
        delegate.loadUShortArray(propertyPath, pathEnd)

    @ExperimentalUnsignedTypes
    override fun loadUIntArray(pathEnd: PropertyName): UIntArray =
        delegate.loadUIntArray(propertyPath, pathEnd)

    @ExperimentalUnsignedTypes
    override fun loadULongArray(pathEnd: PropertyName): ULongArray =
        delegate.loadULongArray(propertyPath, pathEnd)

    override fun loadBooleanArray(pathEnd: PropertyName): BooleanArray =
        delegate.loadBooleanArray(propertyPath, pathEnd)

    override fun loadStringArray(pathEnd: PropertyName): Array<String> =
        delegate.loadStringArray(propertyPath, pathEnd)

    internal class ImmediateNamedPropertyLoader(delegate: PropertyLoader, name: PropertyName) :
        BaseNamedPropertyLoader(
            delegate,
            listOf(name),
            listOf(NameCaseContainer(name)),
            SingleElementPropertyPath(name)
        )

    internal class NestedNamedPropertyLoader(delegate: BaseNamedPropertyLoader, name: PropertyName) :
        BaseNamedPropertyLoader(
            delegate.delegate,
            delegate.names.plusElement(name),
            delegate.precomputedNames + NameCaseContainer(name)
        )
}