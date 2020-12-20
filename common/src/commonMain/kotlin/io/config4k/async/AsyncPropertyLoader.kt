package io.config4k.async

import io.config4k.EmptyPropertyPath
import io.config4k.PropertyName
import io.config4k.PropertyPath

interface AsyncPropertyLoader {
    suspend fun doesNotHave(propertyPath: PropertyPath, pathEnd: PropertyName): Boolean
    suspend fun doesNotHave(pathEnd: PropertyName): Boolean = doesNotHave(EmptyPropertyPath, pathEnd)

    suspend fun loadByte(propertyPath: PropertyPath, pathEnd: PropertyName): Byte
    suspend fun loadShort(propertyPath: PropertyPath, pathEnd: PropertyName): Short
    suspend fun loadInt(propertyPath: PropertyPath, pathEnd: PropertyName): Int
    suspend fun loadLong(propertyPath: PropertyPath, pathEnd: PropertyName): Long
    suspend fun loadFloat(propertyPath: PropertyPath, pathEnd: PropertyName): Float
    suspend fun loadDouble(propertyPath: PropertyPath, pathEnd: PropertyName): Double
    @ExperimentalUnsignedTypes
    suspend fun loadUByte(propertyPath: PropertyPath, pathEnd: PropertyName): UByte
    @ExperimentalUnsignedTypes
    suspend fun loadUShort(propertyPath: PropertyPath, pathEnd: PropertyName): UShort
    @ExperimentalUnsignedTypes
    suspend fun loadUInt(propertyPath: PropertyPath, pathEnd: PropertyName): UInt
    @ExperimentalUnsignedTypes
    suspend fun loadULong(propertyPath: PropertyPath, pathEnd: PropertyName): ULong
    suspend fun loadBoolean(propertyPath: PropertyPath, pathEnd: PropertyName): Boolean
    suspend fun loadString(propertyPath: PropertyPath, pathEnd: PropertyName): String

    suspend fun loadByteArray(propertyPath: PropertyPath, pathEnd: PropertyName): ByteArray
    suspend fun loadShortArray(propertyPath: PropertyPath, pathEnd: PropertyName): ShortArray
    suspend fun loadIntArray(propertyPath: PropertyPath, pathEnd: PropertyName): IntArray
    suspend fun loadLongArray(propertyPath: PropertyPath, pathEnd: PropertyName): LongArray
    suspend fun loadFloatArray(propertyPath: PropertyPath, pathEnd: PropertyName): FloatArray
    suspend fun loadDoubleArray(propertyPath: PropertyPath, pathEnd: PropertyName): DoubleArray
    @ExperimentalUnsignedTypes
    suspend fun loadUByteArray(propertyPath: PropertyPath, pathEnd: PropertyName): UByteArray
    @ExperimentalUnsignedTypes
    suspend fun loadUShortArray(propertyPath: PropertyPath, pathEnd: PropertyName): UShortArray
    @ExperimentalUnsignedTypes
    suspend fun loadUIntArray(propertyPath: PropertyPath, pathEnd: PropertyName): UIntArray
    @ExperimentalUnsignedTypes
    suspend fun loadULongArray(propertyPath: PropertyPath, pathEnd: PropertyName): ULongArray
    suspend fun loadBooleanArray(propertyPath: PropertyPath, pathEnd: PropertyName): BooleanArray
    suspend fun loadStringArray(propertyPath: PropertyPath, pathEnd: PropertyName): Array<String>
    
    suspend fun loadByte(pathEnd: PropertyName): Byte = loadByte(EmptyPropertyPath, pathEnd)
    suspend fun loadShort(pathEnd: PropertyName): Short = loadShort(EmptyPropertyPath, pathEnd)
    suspend fun loadInt(pathEnd: PropertyName): Int = loadInt(EmptyPropertyPath, pathEnd)
    suspend fun loadLong(pathEnd: PropertyName): Long = loadLong(EmptyPropertyPath, pathEnd)
    suspend fun loadFloat(pathEnd: PropertyName): Float = loadFloat(EmptyPropertyPath, pathEnd)
    suspend fun loadDouble(pathEnd: PropertyName): Double = loadDouble(EmptyPropertyPath, pathEnd)
    @ExperimentalUnsignedTypes
    suspend fun loadUByte(pathEnd: PropertyName): UByte = loadUByte(EmptyPropertyPath, pathEnd)
    @ExperimentalUnsignedTypes
    suspend fun loadUShort(pathEnd: PropertyName): UShort = loadUShort(EmptyPropertyPath, pathEnd)
    @ExperimentalUnsignedTypes
    suspend fun loadUInt(pathEnd: PropertyName): UInt = loadUInt(EmptyPropertyPath, pathEnd)
    @ExperimentalUnsignedTypes
    suspend fun loadULong(pathEnd: PropertyName): ULong = loadULong(EmptyPropertyPath, pathEnd)
    suspend fun loadBoolean(pathEnd: PropertyName): Boolean = loadBoolean(EmptyPropertyPath, pathEnd)
    suspend fun loadString(pathEnd: PropertyName): String = loadString(EmptyPropertyPath, pathEnd)

    suspend fun loadByteArray(pathEnd: PropertyName): ByteArray = loadByteArray(EmptyPropertyPath, pathEnd)
    suspend fun loadShortArray(pathEnd: PropertyName): ShortArray = loadShortArray(EmptyPropertyPath, pathEnd)
    suspend fun loadIntArray(pathEnd: PropertyName): IntArray = loadIntArray(EmptyPropertyPath, pathEnd)
    suspend fun loadLongArray(pathEnd: PropertyName): LongArray = loadLongArray(EmptyPropertyPath, pathEnd)
    suspend fun loadFloatArray(pathEnd: PropertyName): FloatArray = loadFloatArray(EmptyPropertyPath, pathEnd)
    suspend fun loadDoubleArray(pathEnd: PropertyName): DoubleArray = loadDoubleArray(EmptyPropertyPath, pathEnd)
    @ExperimentalUnsignedTypes
    suspend fun loadUByteArray(pathEnd: PropertyName): UByteArray = loadUByteArray(EmptyPropertyPath, pathEnd)
    @ExperimentalUnsignedTypes
    suspend fun loadUShortArray(pathEnd: PropertyName): UShortArray = loadUShortArray(EmptyPropertyPath, pathEnd)
    @ExperimentalUnsignedTypes
    suspend fun loadUIntArray(pathEnd: PropertyName): UIntArray = loadUIntArray(EmptyPropertyPath, pathEnd)
    @ExperimentalUnsignedTypes
    suspend fun loadULongArray(pathEnd: PropertyName): ULongArray = loadULongArray(EmptyPropertyPath, pathEnd)
    suspend fun loadBooleanArray(pathEnd: PropertyName): BooleanArray = loadBooleanArray(EmptyPropertyPath, pathEnd)
    suspend fun loadStringArray(pathEnd: PropertyName): Array<String> = loadStringArray(EmptyPropertyPath, pathEnd)
}