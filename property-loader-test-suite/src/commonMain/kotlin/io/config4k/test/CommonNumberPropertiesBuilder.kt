package io.config4k.test

import io.config4k.PropertyName

@Suppress("EXPERIMENTAL_API_USAGE")
interface CommonNumberPropertiesBuilder : PropertiesBuilder {
    fun storeNumber(number: Number, name: PropertyName)
    fun storeNumberArray(numberArray: Array<Number>, name: PropertyName)

    override fun storeByte(byte: Byte, name: PropertyName) = storeNumber(byte, name)
    override fun storeShort(short: Short, name: PropertyName) = storeNumber(short, name)
    override fun storeInt(int: Int, name: PropertyName) = storeNumber(int, name)
    override fun storeLong(long: Long, name: PropertyName) = storeNumber(long, name)
    override fun storeFloat(float: Float, name: PropertyName) = storeNumber(float, name)
    override fun storeDouble(double: Double, name: PropertyName) = storeNumber(double, name)
    override fun storeUByte(uByte: UByte, name: PropertyName) = storeNumber(uByte.toShort(), name)

    override fun storeUShort(uShort: UShort, name: PropertyName) = storeNumber(uShort.toInt(), name)
    override fun storeUInt(uInt: UInt, name: PropertyName) = storeNumber(uInt.toLong(), name)
    override fun storeULong(uLong: ULong, name: PropertyName) = storeNumber(uLong.toLong(), name)

    private inline fun <T, U : Number> T.toNumberArray(size: T.() -> Int, get: T.(Int) -> U): Array<Number> {
        return Array(size()) {
            get(it)
        }
    }

    override fun storeByteArray(byteArray: ByteArray, name: PropertyName) =
        storeNumberArray(byteArray.toNumberArray({ size }) { get(it) }, name)

    override fun storeShortArray(shortArray: ShortArray, name: PropertyName) =
        storeNumberArray(shortArray.toNumberArray({ size }) { get(it) }, name)

    override fun storeIntArray(intArray: IntArray, name: PropertyName) =
        storeNumberArray(intArray.toNumberArray({ size }) { get(it) }, name)

    override fun storeLongArray(longArray: LongArray, name: PropertyName) =
        storeNumberArray(longArray.toNumberArray({ size }) { get(it) }, name)

    override fun storeFloatArray(floatArray: FloatArray, name: PropertyName) =
        storeNumberArray(floatArray.toNumberArray({ size }) { get(it) }, name)

    override fun storeDoubleArray(doubleArray: DoubleArray, name: PropertyName) =
        storeNumberArray(doubleArray.toNumberArray({ size }) { get(it) }, name)

    override fun storeUByteArray(uByteArray: UByteArray, name: PropertyName) =
        storeNumberArray(uByteArray.toNumberArray({ size }) { get(it).toShort() }, name)

    override fun storeUShortArray(uShortArray: UShortArray, name: PropertyName) =
        storeNumberArray(uShortArray.toNumberArray({ size }) { get(it).toInt() }, name)

    override fun storeUIntArray(uIntArray: UIntArray, name: PropertyName) =
        storeNumberArray(uIntArray.toNumberArray({ size }) { get(it).toLong() }, name)

    override fun storeULongArray(uLongArray: ULongArray, name: PropertyName) =
        storeNumberArray(uLongArray.toNumberArray({ size }) { get(it).toLong() }, name)
}