package io.config4k.json

import io.config4k.*
import io.config4k.blocking.PropertyLoader
import io.config4k.blocking.StringLoaderFactory
import io.config4k.blocking.StructuredPropertyLoader
import kotlinx.serialization.json.*

class JsonPropertyLoader(private val json: JsonObject, private val case: NameCase, private val prevPath: String = "") :
    StructuredPropertyLoader {
    class Factory(private val case: NameCase): StringLoaderFactory {
        private val json = Json
        override fun get(string: String): PropertyLoader {
            return try {
                JsonPropertyLoader(
                    json.parseToJsonElement(string)
                        .let { if (it !is JsonObject) throw IllegalArgumentException("expected json object, got $it") else it},
                    case,
                )
            } catch (e: Throwable) {
                if (e is IllegalArgumentException) throw e
                else throw IllegalArgumentException("expected json object, but got $string")
            }
        }
    }

    companion object : StringLoaderFactory by Factory(NameCase.SnakeCase)

    private fun at(propertyPath: PropertyPath): JsonObject {
        var current = json
        for ((i, element) in propertyPath.preComputedNamesFor(case).withIndex()) {
            val new = current[element]?: throw LazyMissingPropertyExceptionImpl(propertyPath, null, case, "json object", i)
            if (new !is JsonObject)
                throw LazyWrongPropertyTypeExceptionImpl(propertyPath, null, case, "json object") {new.toString()}
            current = new
        }
        return current
    }

    private fun get(
        propertyPath: PropertyPath,
        pathEnd: PropertyName,
        expected: String,
        array: Boolean = false
    ): JsonElement {
        return at(propertyPath)[case.computeNameFor(pathEnd)] ?: throw LazyMissingPropertyExceptionImpl(propertyPath, pathEnd, case, expected, an = array)
    }

    private fun JsonElement.primitive(
        expected: String,
        propertyPath: PropertyPath,
        pathEnd: PropertyName
    ): JsonPrimitive {
        if (this !is JsonPrimitive) throw LazyWrongPropertyTypeExceptionImpl(propertyPath, pathEnd, case, expected) {
            this.toString()
        }
        return this
    }

    private inline fun <T> JsonElement.number(
        expected: String,
        propertyPath: PropertyPath,
        pathEnd: PropertyName,
        withIndex: Int? = null,
        block: JsonPrimitive.() -> T
    ): T {
        val primitive = primitive(expected, propertyPath, pathEnd)
        return try {
            primitive.block()
        } catch (e: NumberFormatException) {
            throw WrongPropertyTypeExceptionImpl(propertyPath, pathEnd, case, expected, primitive.toString(), withIndex = withIndex)
        }
    }

    private inline fun <T> numberAt(
        expected: String,
        propertyPath: PropertyPath,
        pathEnd: PropertyName,
        block: JsonPrimitive.() -> T
    ): T {
        return get(propertyPath, pathEnd, expected).number(expected, propertyPath, pathEnd,null, block)
    }

    private fun array(propertyPath: PropertyPath, pathEnd: PropertyName, expected: String): JsonArray {
        return get(propertyPath, pathEnd, expected, true).let {
            if (it !is JsonArray) throw LazyWrongPropertyTypeExceptionImpl(propertyPath, pathEnd, case, expected, an = true) {
                it.toString()
            }
            else it
        }
    }

    private inline fun <T, U> loadArray(
        propertyPath: PropertyPath,
        pathEnd: PropertyName,
        expectedArray: String,
        array: (Int, (Int) -> U) -> T,
        crossinline block: JsonElement.(Int) -> U
    ): T {
        val jArray = array(propertyPath, pathEnd, expectedArray)
        return array(jArray.size) {
            val jElement = jArray[it]
            jElement.block(it)
        }
    }
    private inline fun <T, U> loadNumberArray(
        propertyPath: PropertyPath,
        pathEnd: PropertyName,
        expectedArray: String,
        expected: String,
        array: (Int, (Int) -> U) -> T,
        crossinline block: JsonPrimitive.() -> U
    ): T {
        return loadArray(propertyPath, pathEnd, expectedArray, array) {
            number(expected, propertyPath, pathEnd, it, block)
        }
    }

    override fun doesNotHave(propertyPath: PropertyPath, pathEnd: PropertyName): Boolean {
        var current = json
        for (element in propertyPath.preComputedNamesFor(case)) {
            current = current[element]?.let { it as? JsonObject }?: return true
        }
        return current.containsKey(case.computeNameFor(pathEnd))
    }

    override fun advance(name: PropertyName): PropertyLoader {
        val stringName = case.computeNameFor(name)
        val new = json[stringName]?: throw CustomMessageMissingPropertyException(
            "expected json object at ${if (prevPath.isEmpty()) "" else "$prevPath."}$stringName"
        )
        if (new !is JsonObject) throw CustomMessageWrongPropertyException(
            "expected json object at a ${if (prevPath.isEmpty()) "" else "$prevPath."}$stringName"
        )
        return JsonPropertyLoader(new, case, stringName)
    }

    override fun loadByte(propertyPath: PropertyPath, pathEnd: PropertyName): Byte =
        numberAt(PropertyTypes.BYTE, propertyPath, pathEnd) { int.toByte() }

    override fun loadShort(propertyPath: PropertyPath, pathEnd: PropertyName): Short =
        numberAt(PropertyTypes.SHORT, propertyPath, pathEnd) { int.toShort() }

    override fun loadInt(propertyPath: PropertyPath, pathEnd: PropertyName): Int =
        numberAt(PropertyTypes.INT, propertyPath, pathEnd) { int }

    override fun loadLong(propertyPath: PropertyPath, pathEnd: PropertyName): Long =
        numberAt(PropertyTypes.LONG, propertyPath, pathEnd) { long }

    override fun loadFloat(propertyPath: PropertyPath, pathEnd: PropertyName): Float =
        numberAt(PropertyTypes.BYTE, propertyPath, pathEnd) { float }

    override fun loadDouble(propertyPath: PropertyPath, pathEnd: PropertyName): Double =
        numberAt(PropertyTypes.DOUBLE, propertyPath, pathEnd) { double }

    @ExperimentalUnsignedTypes
    override fun loadUByte(propertyPath: PropertyPath, pathEnd: PropertyName): UByte =
        numberAt(PropertyTypes.U_BYTE, propertyPath, pathEnd) { content.toUByte() }

    @ExperimentalUnsignedTypes
    override fun loadUShort(propertyPath: PropertyPath, pathEnd: PropertyName): UShort =
        numberAt(PropertyTypes.U_SHORT, propertyPath, pathEnd) { content.toUShort() }

    @ExperimentalUnsignedTypes
    override fun loadUInt(propertyPath: PropertyPath, pathEnd: PropertyName): UInt =
        numberAt(PropertyTypes.U_INT, propertyPath, pathEnd) { content.toUInt() }

    @ExperimentalUnsignedTypes
    override fun loadULong(propertyPath: PropertyPath, pathEnd: PropertyName): ULong =
        numberAt(PropertyTypes.U_LONG, propertyPath, pathEnd) { content.toULong() }

    override fun loadBoolean(propertyPath: PropertyPath, pathEnd: PropertyName): Boolean {
        val primitive = get(propertyPath, pathEnd, PropertyTypes.BOOLEAN).primitive(PropertyTypes.BOOLEAN, propertyPath, pathEnd)
        return try {
            primitive.boolean
        } catch (e: IllegalStateException) {
            throw WrongPropertyTypeExceptionImpl(propertyPath, pathEnd, case, PropertyTypes.BOOLEAN, primitive.toString())
        }
    }

    override fun loadString(propertyPath: PropertyPath, pathEnd: PropertyName): String {
        return get(propertyPath, pathEnd, PropertyTypes.STRING).primitive(PropertyTypes.STRING, propertyPath, pathEnd).content
    }

    override fun loadByteArray(propertyPath: PropertyPath, pathEnd: PropertyName): ByteArray =
        loadNumberArray(propertyPath, pathEnd, PropertyTypes.BYTE_ARR, PropertyTypes.BYTE, ::ByteArray) {int.toByte()}

    override fun loadShortArray(propertyPath: PropertyPath, pathEnd: PropertyName): ShortArray =
        loadNumberArray(propertyPath, pathEnd, PropertyTypes.SHORT_ARR, PropertyTypes.SHORT, ::ShortArray) {int.toShort()}

    override fun loadIntArray(propertyPath: PropertyPath, pathEnd: PropertyName): IntArray =
        loadNumberArray(propertyPath, pathEnd, PropertyTypes.INT_ARR, PropertyTypes.INT, ::IntArray) {int}

    override fun loadLongArray(propertyPath: PropertyPath, pathEnd: PropertyName): LongArray =
        loadNumberArray(propertyPath, pathEnd, PropertyTypes.LONG_ARR, PropertyTypes.LONG, ::LongArray) {long}

    override fun loadFloatArray(propertyPath: PropertyPath, pathEnd: PropertyName): FloatArray =
        loadNumberArray(propertyPath, pathEnd, PropertyTypes.FLOAT_ARR, PropertyTypes.FLOAT, ::FloatArray) {float}

    override fun loadDoubleArray(propertyPath: PropertyPath, pathEnd: PropertyName): DoubleArray =
        loadNumberArray(propertyPath, pathEnd, PropertyTypes.DOUBLE_ARR, PropertyTypes.DOUBLE, ::DoubleArray) {double}

    @ExperimentalUnsignedTypes
    override fun loadUByteArray(propertyPath: PropertyPath, pathEnd: PropertyName): UByteArray =
        loadNumberArray(propertyPath, pathEnd, PropertyTypes.U_BYTE_ARR, PropertyTypes.U_BYTE, ::UByteArray) {content.toUByte()}

    @ExperimentalUnsignedTypes
    override fun loadUShortArray(propertyPath: PropertyPath, pathEnd: PropertyName): UShortArray =
        loadNumberArray(propertyPath, pathEnd, PropertyTypes.U_SHORT_ARR, PropertyTypes.SHORT, ::UShortArray) {content.toUShort()}

    @ExperimentalUnsignedTypes
    override fun loadUIntArray(propertyPath: PropertyPath, pathEnd: PropertyName): UIntArray =
        loadNumberArray(propertyPath, pathEnd, PropertyTypes.U_INT_ARR, PropertyTypes.U_INT, ::UIntArray) {content.toUInt()}

    @ExperimentalUnsignedTypes
    override fun loadULongArray(propertyPath: PropertyPath, pathEnd: PropertyName): ULongArray =
        loadNumberArray(propertyPath, pathEnd, PropertyTypes.U_LONG_ARR, PropertyTypes.U_LONG, ::ULongArray) {content.toULong()}

    override fun loadBooleanArray(propertyPath: PropertyPath, pathEnd: PropertyName): BooleanArray =
        loadArray(propertyPath, pathEnd, PropertyTypes.BOOLEAN_ARR, ::BooleanArray) {
            try {
                primitive(PropertyTypes.BOOLEAN, propertyPath, pathEnd).boolean
            } catch (e: IllegalStateException) {
                throw WrongPropertyTypeExceptionImpl(propertyPath, pathEnd, case, PropertyTypes.BOOLEAN, it.toString(), withIndex = it)
            }
        }

    override fun loadStringArray(propertyPath: PropertyPath, pathEnd: PropertyName): Array<String> =
        loadArray(propertyPath, pathEnd, PropertyTypes.STRING_ARR, ::Array) {
            primitive(PropertyTypes.STRING, propertyPath, pathEnd).content
        }
}