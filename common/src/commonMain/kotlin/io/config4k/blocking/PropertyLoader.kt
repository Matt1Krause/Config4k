package io.config4k.blocking

import io.config4k.EmptyPropertyPath
import io.config4k.PropertyName
import io.config4k.PropertyPath

interface PropertyLoader {
    fun doesNotHave(propertyPath: PropertyPath, pathEnd: PropertyName): Boolean
    fun doesNotHave(pathEnd: PropertyName): Boolean = doesNotHave(EmptyPropertyPath, pathEnd)
    fun loadByte(propertyPath: PropertyPath, pathEnd: PropertyName): Byte
    fun loadShort(propertyPath: PropertyPath, pathEnd: PropertyName): Short
    fun loadInt(propertyPath: PropertyPath, pathEnd: PropertyName): Int
    fun loadLong(propertyPath: PropertyPath, pathEnd: PropertyName): Long
    fun loadFloat(propertyPath: PropertyPath, pathEnd: PropertyName): Float
    fun loadDouble(propertyPath: PropertyPath, pathEnd: PropertyName): Double
    @ExperimentalUnsignedTypes
    fun loadUByte(propertyPath: PropertyPath, pathEnd: PropertyName): UByte
    @ExperimentalUnsignedTypes
    fun loadUShort(propertyPath: PropertyPath, pathEnd: PropertyName): UShort
    @ExperimentalUnsignedTypes
    fun loadUInt(propertyPath: PropertyPath, pathEnd: PropertyName): UInt
    @ExperimentalUnsignedTypes
    fun loadULong(propertyPath: PropertyPath, pathEnd: PropertyName): ULong
    fun loadBoolean(propertyPath: PropertyPath, pathEnd: PropertyName): Boolean
    fun loadString(propertyPath: PropertyPath, pathEnd: PropertyName): String

    fun loadByteArray(propertyPath: PropertyPath, pathEnd: PropertyName): ByteArray
    fun loadShortArray(propertyPath: PropertyPath, pathEnd: PropertyName): ShortArray
    fun loadIntArray(propertyPath: PropertyPath, pathEnd: PropertyName): IntArray
    fun loadLongArray(propertyPath: PropertyPath, pathEnd: PropertyName): LongArray
    fun loadFloatArray(propertyPath: PropertyPath, pathEnd: PropertyName): FloatArray
    fun loadDoubleArray(propertyPath: PropertyPath, pathEnd: PropertyName): DoubleArray
    @ExperimentalUnsignedTypes
    fun loadUByteArray(propertyPath: PropertyPath, pathEnd: PropertyName): UByteArray
    @ExperimentalUnsignedTypes
    fun loadUShortArray(propertyPath: PropertyPath, pathEnd: PropertyName): UShortArray
    @ExperimentalUnsignedTypes
    fun loadUIntArray(propertyPath: PropertyPath, pathEnd: PropertyName): UIntArray
    @ExperimentalUnsignedTypes
    fun loadULongArray(propertyPath: PropertyPath, pathEnd: PropertyName): ULongArray
    fun loadBooleanArray(propertyPath: PropertyPath, pathEnd: PropertyName): BooleanArray
    fun loadStringArray(propertyPath: PropertyPath, pathEnd: PropertyName): Array<String>

    fun loadByte(pathEnd: PropertyName): Byte = loadByte(EmptyPropertyPath, pathEnd)
    fun loadShort(pathEnd: PropertyName): Short = loadShort(EmptyPropertyPath, pathEnd)
    fun loadInt(pathEnd: PropertyName): Int = loadInt(EmptyPropertyPath, pathEnd)
    fun loadLong(pathEnd: PropertyName): Long = loadLong(EmptyPropertyPath, pathEnd)
    fun loadFloat(pathEnd: PropertyName): Float = loadFloat(EmptyPropertyPath, pathEnd)
    fun loadDouble(pathEnd: PropertyName): Double = loadDouble(EmptyPropertyPath, pathEnd)
    @ExperimentalUnsignedTypes
    fun loadUByte(pathEnd: PropertyName): UByte = loadUByte(EmptyPropertyPath, pathEnd)
    @ExperimentalUnsignedTypes
    fun loadUShort(pathEnd: PropertyName): UShort = loadUShort(EmptyPropertyPath, pathEnd)
    @ExperimentalUnsignedTypes
    fun loadUInt(pathEnd: PropertyName): UInt = loadUInt(EmptyPropertyPath, pathEnd)
    @ExperimentalUnsignedTypes
    fun loadULong(pathEnd: PropertyName): ULong = loadULong(EmptyPropertyPath, pathEnd)
    fun loadBoolean(pathEnd: PropertyName): Boolean = loadBoolean(EmptyPropertyPath, pathEnd)
    fun loadString(pathEnd: PropertyName): String = loadString(EmptyPropertyPath, pathEnd)

    fun loadByteArray(pathEnd: PropertyName): ByteArray = loadByteArray(EmptyPropertyPath, pathEnd)
    fun loadShortArray(pathEnd: PropertyName): ShortArray = loadShortArray(EmptyPropertyPath, pathEnd)
    fun loadIntArray(pathEnd: PropertyName): IntArray = loadIntArray(EmptyPropertyPath, pathEnd)
    fun loadLongArray(pathEnd: PropertyName): LongArray = loadLongArray(EmptyPropertyPath, pathEnd)
    fun loadFloatArray(pathEnd: PropertyName): FloatArray = loadFloatArray(EmptyPropertyPath, pathEnd)
    fun loadDoubleArray(pathEnd: PropertyName): DoubleArray = loadDoubleArray(EmptyPropertyPath, pathEnd)
    @ExperimentalUnsignedTypes
    fun loadUByteArray(pathEnd: PropertyName): UByteArray = loadUByteArray(EmptyPropertyPath, pathEnd)
    @ExperimentalUnsignedTypes
    fun loadUShortArray(pathEnd: PropertyName): UShortArray = loadUShortArray(EmptyPropertyPath, pathEnd)
    @ExperimentalUnsignedTypes
    fun loadUIntArray(pathEnd: PropertyName): UIntArray = loadUIntArray(EmptyPropertyPath, pathEnd)
    @ExperimentalUnsignedTypes
    fun loadULongArray(pathEnd: PropertyName): ULongArray = loadULongArray(EmptyPropertyPath, pathEnd)
    fun loadBooleanArray(pathEnd: PropertyName): BooleanArray = loadBooleanArray(EmptyPropertyPath, pathEnd)
    fun loadStringArray(pathEnd: PropertyName): Array<String> = loadStringArray(EmptyPropertyPath, pathEnd)
}