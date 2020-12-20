package io.config4k.test

import io.config4k.PropertyName
import io.config4k.async.AsyncPropertyLoader

@Suppress("EXPERIMENTAL_API_USAGE")
interface PropertiesBuilder {
    fun storeByte(byte: Byte, name: PropertyName)
    fun storeShort(short: Short, name: PropertyName)
    fun storeInt(int: Int, name: PropertyName)
    fun storeLong(long: Long, name: PropertyName)
    fun storeFloat(float: Float, name: PropertyName)
    fun storeDouble(double: Double, name: PropertyName)
    fun storeUByte(uByte: UByte, name: PropertyName)
    fun storeUShort(uShort: UShort, name: PropertyName)
    fun storeUInt(uInt: UInt, name: PropertyName)
    fun storeULong(uLong: ULong, name: PropertyName)
    fun storeBoolean(boolean: Boolean, name: PropertyName)
    fun storeString(string: String, name: PropertyName)

    fun storeByteArray(byteArray: ByteArray, name: PropertyName)
    fun storeShortArray(shortArray: ShortArray, name: PropertyName)
    fun storeIntArray(intArray: IntArray, name: PropertyName)
    fun storeLongArray(longArray: LongArray, name: PropertyName)
    fun storeFloatArray(floatArray: FloatArray, name: PropertyName)
    fun storeDoubleArray(doubleArray: DoubleArray, name: PropertyName)
    fun storeUByteArray(uByteArray: UByteArray, name: PropertyName)
    fun storeUShortArray(uShortArray: UShortArray, name: PropertyName)
    fun storeUIntArray(uIntArray: UIntArray, name: PropertyName)
    fun storeULongArray(uLongArray: ULongArray, name: PropertyName)
    fun storeBooleanArray(booleanArray: BooleanArray, name: PropertyName)
    fun storeStringArray(stringArray: Array<String>, name: PropertyName)

    fun advance(name: PropertyName): PropertiesBuilder

    fun get(): AsyncPropertyLoader
}