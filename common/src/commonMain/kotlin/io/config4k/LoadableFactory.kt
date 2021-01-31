@file:JvmName("LoadableFactoryUtil")

package io.config4k

import io.config4k.async.AsyncComplexLoadableFactory
import io.config4k.async.AsyncLoadableFactory
import io.config4k.async.AsyncPropertyLoader
import io.config4k.blocking.BlockingComplexLoadableFactory
import io.config4k.blocking.BlockingLoadableFactory
import io.config4k.blocking.PropertyLoader
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

interface LoadableFactory<T> : BlockingLoadableFactory<T>, AsyncLoadableFactory<T>
interface ComplexLoadableFactory<T> : LoadableFactory<T>, BlockingComplexLoadableFactory<T>,
    AsyncComplexLoadableFactory<T>

/*
types = ["Byte", "Short", "Int", "Long", "Float", "Double", "Boolean", "String", "UByte", "UShort", "UInt", "ULong"]
def loadString(type):
    return "loader.load" + type + "(at)"
for type in types:
    print("object " + type + "Loader: PrimitiveLoadableFactory<" + type + ">() {")
    print("override suspend fun load(loader: AsyncPropertyLoader, at: Array<String>): " + type + "=" )
    print(loadString(type))
    print("override fun load(loader: PropertyLoader, at: Array<String>): " + type + "=")
    print(loadString(type))
    print("}")

    print("object " + type + "ArrayLoader: PrimitiveLoadableFactory<" + type + "Array>() {")
    print("override suspend fun load(loader: AsyncPropertyLoader, at: Array<String>): " + type + "Array =" )
    print(loadString(type + "Array"))
    print("override fun load(loader: PropertyLoader, at: Array<String>): " + type + "Array =")
    print(loadString(type + "Array"))
    print("}")

    print("object " + type + "GArrayLoader: PrimitiveLoadableFactory<Array<" + type + ">>() {")
    print("override suspend fun load(loader: AsyncPropertyLoader, at: Array<String>): Array<" + type + "> =" )
    print(loadString(type + "Array") + ".toTypedArray()")
    print("override fun load(loader: PropertyLoader, at: Array<String>): Array<" + type + "> =")
    print(loadString(type + "Array") + ".toTypedArray()")
    print("}")

 */
@Suppress("unused")
@OptIn(ExperimentalUnsignedTypes::class)
sealed class PrimitiveLoadableFactory<T> : LoadableFactory<T> {
    object ByteLoader : PrimitiveLoadableFactory<Byte>() {
        override suspend fun load(loader: AsyncPropertyLoader, at: Array<String>): Byte =
            loader.loadByte(at)

        override fun load(loader: PropertyLoader, at: Array<String>): Byte =
            loader.loadByte(at)
    }

    object ByteArrayLoader : PrimitiveLoadableFactory<ByteArray>() {
        override suspend fun load(loader: AsyncPropertyLoader, at: Array<String>): ByteArray =
            loader.loadByteArray(at)

        override fun load(loader: PropertyLoader, at: Array<String>): ByteArray =
            loader.loadByteArray(at)
    }

    object ByteGArrayLoader : PrimitiveLoadableFactory<Array<Byte>>() {
        override suspend fun load(loader: AsyncPropertyLoader, at: Array<String>): Array<Byte> =
            loader.loadByteArray(at).toTypedArray()

        override fun load(loader: PropertyLoader, at: Array<String>): Array<Byte> =
            loader.loadByteArray(at).toTypedArray()
    }

    object ShortLoader : PrimitiveLoadableFactory<Short>() {
        override suspend fun load(loader: AsyncPropertyLoader, at: Array<String>): Short =
            loader.loadShort(at)

        override fun load(loader: PropertyLoader, at: Array<String>): Short =
            loader.loadShort(at)
    }

    object ShortArrayLoader : PrimitiveLoadableFactory<ShortArray>() {
        override suspend fun load(loader: AsyncPropertyLoader, at: Array<String>): ShortArray =
            loader.loadShortArray(at)

        override fun load(loader: PropertyLoader, at: Array<String>): ShortArray =
            loader.loadShortArray(at)
    }

    object ShortGArrayLoader : PrimitiveLoadableFactory<Array<Short>>() {
        override suspend fun load(loader: AsyncPropertyLoader, at: Array<String>): Array<Short> =
            loader.loadShortArray(at).toTypedArray()

        override fun load(loader: PropertyLoader, at: Array<String>): Array<Short> =
            loader.loadShortArray(at).toTypedArray()
    }

    object IntLoader : PrimitiveLoadableFactory<Int>() {
        override suspend fun load(loader: AsyncPropertyLoader, at: Array<String>): Int =
            loader.loadInt(at)

        override fun load(loader: PropertyLoader, at: Array<String>): Int =
            loader.loadInt(at)
    }

    object IntArrayLoader : PrimitiveLoadableFactory<IntArray>() {
        override suspend fun load(loader: AsyncPropertyLoader, at: Array<String>): IntArray =
            loader.loadIntArray(at)

        override fun load(loader: PropertyLoader, at: Array<String>): IntArray =
            loader.loadIntArray(at)
    }

    object IntGArrayLoader : PrimitiveLoadableFactory<Array<Int>>() {
        override suspend fun load(loader: AsyncPropertyLoader, at: Array<String>): Array<Int> =
            loader.loadIntArray(at).toTypedArray()

        override fun load(loader: PropertyLoader, at: Array<String>): Array<Int> =
            loader.loadIntArray(at).toTypedArray()
    }

    object LongLoader : PrimitiveLoadableFactory<Long>() {
        override suspend fun load(loader: AsyncPropertyLoader, at: Array<String>): Long =
            loader.loadLong(at)

        override fun load(loader: PropertyLoader, at: Array<String>): Long =
            loader.loadLong(at)
    }

    object LongArrayLoader : PrimitiveLoadableFactory<LongArray>() {
        override suspend fun load(loader: AsyncPropertyLoader, at: Array<String>): LongArray =
            loader.loadLongArray(at)

        override fun load(loader: PropertyLoader, at: Array<String>): LongArray =
            loader.loadLongArray(at)
    }

    object LongGArrayLoader : PrimitiveLoadableFactory<Array<Long>>() {
        override suspend fun load(loader: AsyncPropertyLoader, at: Array<String>): Array<Long> =
            loader.loadLongArray(at).toTypedArray()

        override fun load(loader: PropertyLoader, at: Array<String>): Array<Long> =
            loader.loadLongArray(at).toTypedArray()
    }

    object FloatLoader : PrimitiveLoadableFactory<Float>() {
        override suspend fun load(loader: AsyncPropertyLoader, at: Array<String>): Float =
            loader.loadFloat(at)

        override fun load(loader: PropertyLoader, at: Array<String>): Float =
            loader.loadFloat(at)
    }

    object FloatArrayLoader : PrimitiveLoadableFactory<FloatArray>() {
        override suspend fun load(loader: AsyncPropertyLoader, at: Array<String>): FloatArray =
            loader.loadFloatArray(at)

        override fun load(loader: PropertyLoader, at: Array<String>): FloatArray =
            loader.loadFloatArray(at)
    }

    object FloatGArrayLoader : PrimitiveLoadableFactory<Array<Float>>() {
        override suspend fun load(loader: AsyncPropertyLoader, at: Array<String>): Array<Float> =
            loader.loadFloatArray(at).toTypedArray()

        override fun load(loader: PropertyLoader, at: Array<String>): Array<Float> =
            loader.loadFloatArray(at).toTypedArray()
    }

    object DoubleLoader : PrimitiveLoadableFactory<Double>() {
        override suspend fun load(loader: AsyncPropertyLoader, at: Array<String>): Double =
            loader.loadDouble(at)

        override fun load(loader: PropertyLoader, at: Array<String>): Double =
            loader.loadDouble(at)
    }

    object DoubleArrayLoader : PrimitiveLoadableFactory<DoubleArray>() {
        override suspend fun load(loader: AsyncPropertyLoader, at: Array<String>): DoubleArray =
            loader.loadDoubleArray(at)

        override fun load(loader: PropertyLoader, at: Array<String>): DoubleArray =
            loader.loadDoubleArray(at)
    }

    object DoubleGArrayLoader : PrimitiveLoadableFactory<Array<Double>>() {
        override suspend fun load(loader: AsyncPropertyLoader, at: Array<String>): Array<Double> =
            loader.loadDoubleArray(at).toTypedArray()

        override fun load(loader: PropertyLoader, at: Array<String>): Array<Double> =
            loader.loadDoubleArray(at).toTypedArray()
    }

    object BooleanLoader : PrimitiveLoadableFactory<Boolean>() {
        override suspend fun load(loader: AsyncPropertyLoader, at: Array<String>): Boolean =
            loader.loadBoolean(at)

        override fun load(loader: PropertyLoader, at: Array<String>): Boolean =
            loader.loadBoolean(at)
    }

    object BooleanArrayLoader : PrimitiveLoadableFactory<BooleanArray>() {
        override suspend fun load(loader: AsyncPropertyLoader, at: Array<String>): BooleanArray =
            loader.loadBooleanArray(at)

        override fun load(loader: PropertyLoader, at: Array<String>): BooleanArray =
            loader.loadBooleanArray(at)
    }

    object BooleanGArrayLoader : PrimitiveLoadableFactory<Array<Boolean>>() {
        override suspend fun load(loader: AsyncPropertyLoader, at: Array<String>): Array<Boolean> =
            loader.loadBooleanArray(at).toTypedArray()

        override fun load(loader: PropertyLoader, at: Array<String>): Array<Boolean> =
            loader.loadBooleanArray(at).toTypedArray()
    }

    object StringLoader : PrimitiveLoadableFactory<String>() {
        override suspend fun load(loader: AsyncPropertyLoader, at: Array<String>): String =
            loader.loadString(at)

        override fun load(loader: PropertyLoader, at: Array<String>): String =
            loader.loadString(at)
    }

    object StringArrayLoader : PrimitiveLoadableFactory<Array<String>>() {
        override suspend fun load(loader: AsyncPropertyLoader, at: Array<String>): Array<String> =
            loader.loadStringArray(at)

        override fun load(loader: PropertyLoader, at: Array<String>): Array<String> =
            loader.loadStringArray(at)
    }

    object UByteLoader : PrimitiveLoadableFactory<UByte>() {
        override suspend fun load(loader: AsyncPropertyLoader, at: Array<String>): UByte =
            loader.loadUByte(at)

        override fun load(loader: PropertyLoader, at: Array<String>): UByte =
            loader.loadUByte(at)
    }

    object UByteArrayLoader : PrimitiveLoadableFactory<UByteArray>() {
        override suspend fun load(loader: AsyncPropertyLoader, at: Array<String>): UByteArray =
            loader.loadUByteArray(at)

        override fun load(loader: PropertyLoader, at: Array<String>): UByteArray =
            loader.loadUByteArray(at)
    }

    object UByteGArrayLoader : PrimitiveLoadableFactory<Array<UByte>>() {
        override suspend fun load(loader: AsyncPropertyLoader, at: Array<String>): Array<UByte> =
            loader.loadUByteArray(at).toTypedArray()

        override fun load(loader: PropertyLoader, at: Array<String>): Array<UByte> =
            loader.loadUByteArray(at).toTypedArray()
    }

    object UShortLoader : PrimitiveLoadableFactory<UShort>() {
        override suspend fun load(loader: AsyncPropertyLoader, at: Array<String>): UShort =
            loader.loadUShort(at)

        override fun load(loader: PropertyLoader, at: Array<String>): UShort =
            loader.loadUShort(at)
    }

    object UShortArrayLoader : PrimitiveLoadableFactory<UShortArray>() {
        override suspend fun load(loader: AsyncPropertyLoader, at: Array<String>): UShortArray =
            loader.loadUShortArray(at)

        override fun load(loader: PropertyLoader, at: Array<String>): UShortArray =
            loader.loadUShortArray(at)
    }

    object UShortGArrayLoader : PrimitiveLoadableFactory<Array<UShort>>() {
        override suspend fun load(loader: AsyncPropertyLoader, at: Array<String>): Array<UShort> =
            loader.loadUShortArray(at).toTypedArray()

        override fun load(loader: PropertyLoader, at: Array<String>): Array<UShort> =
            loader.loadUShortArray(at).toTypedArray()
    }

    object UIntLoader : PrimitiveLoadableFactory<UInt>() {
        override suspend fun load(loader: AsyncPropertyLoader, at: Array<String>): UInt =
            loader.loadUInt(at)

        override fun load(loader: PropertyLoader, at: Array<String>): UInt =
            loader.loadUInt(at)
    }

    object UIntArrayLoader : PrimitiveLoadableFactory<UIntArray>() {
        override suspend fun load(loader: AsyncPropertyLoader, at: Array<String>): UIntArray =
            loader.loadUIntArray(at)

        override fun load(loader: PropertyLoader, at: Array<String>): UIntArray =
            loader.loadUIntArray(at)
    }

    object UIntGArrayLoader : PrimitiveLoadableFactory<Array<UInt>>() {
        override suspend fun load(loader: AsyncPropertyLoader, at: Array<String>): Array<UInt> =
            loader.loadUIntArray(at).toTypedArray()

        override fun load(loader: PropertyLoader, at: Array<String>): Array<UInt> =
            loader.loadUIntArray(at).toTypedArray()
    }

    object ULongLoader : PrimitiveLoadableFactory<ULong>() {
        override suspend fun load(loader: AsyncPropertyLoader, at: Array<String>): ULong =
            loader.loadULong(at)

        override fun load(loader: PropertyLoader, at: Array<String>): ULong =
            loader.loadULong(at)
    }

    object ULongArrayLoader : PrimitiveLoadableFactory<ULongArray>() {
        override suspend fun load(loader: AsyncPropertyLoader, at: Array<String>): ULongArray =
            loader.loadULongArray(at)

        override fun load(loader: PropertyLoader, at: Array<String>): ULongArray =
            loader.loadULongArray(at)
    }

    object ULongGArrayLoader : PrimitiveLoadableFactory<Array<ULong>>() {
        override suspend fun load(loader: AsyncPropertyLoader, at: Array<String>): Array<ULong> =
            loader.loadULongArray(at).toTypedArray()

        override fun load(loader: PropertyLoader, at: Array<String>): Array<ULong> =
            loader.loadULongArray(at).toTypedArray()
    }
}

@JvmName("loadSafe")
operator fun <T> BlockingLoadableFactory<T>.invoke(loader: PropertyLoader, at: PropertyName, inline: Boolean): T {
    if (this is PrimitiveLoadableFactory<T>) return load(loader, at)
    val usedLoader = if (inline) loader else loader.nested(at)
    return if (this is ComplexLoadableFactory<T>) load(usedLoader) else load(usedLoader, at)
}

suspend operator fun <T> AsyncLoadableFactory<T>.invoke(
    loader: AsyncPropertyLoader,
    at: PropertyName,
    inline: Boolean
): T {
    if (this is PrimitiveLoadableFactory<T>) return load(loader, at)
    val usedLoader = if (inline) loader else loader.asyncNested(at)
    return if (this is ComplexLoadableFactory<T>) load(usedLoader) else load(usedLoader, at)
}

@JvmSynthetic
inline operator fun <T> BlockingComplexLoadableFactory<T>.invoke(loader: PropertyLoader): T = load(loader)
@JvmSynthetic
suspend inline operator fun <T> AsyncComplexLoadableFactory<T>.invoke(loader: AsyncPropertyLoader): T = load(loader)

