package io.config4k.test

import io.config4k.PropertyName

@Suppress("EXPERIMENTAL_API_USAGE")
abstract class StringifyPropertiesBuilder(private val quoteString: Boolean) : CommonNumberPropertiesBuilder {
    protected abstract fun storeAsString(string: String, name: PropertyName)
    protected abstract fun storeAsString(strings: List<String>, name: PropertyName)
    private inline fun <T> storeAsString(obj: T, name: PropertyName) = storeAsString(obj.toString(), name)

    override fun storeNumber(number: Number, name: PropertyName) {
        storeAsString(number.toString(), name)
    }

    override fun storeBoolean(boolean: Boolean, name: PropertyName) = storeAsString(boolean, name)

    override fun storeString(string: String, name: PropertyName) = if (quoteString)
        storeAsString("\"$string\"", name)
    else
        storeAsString(string, name)

    override fun storeNumberArray(numberArray: Array<Number>, name: PropertyName) {
        storeAsString(numberArray.map {it.toString()}, name)
    }

    override fun storeBooleanArray(booleanArray: BooleanArray, name: PropertyName) =
        storeAsString(booleanArray.map(Any::toString), name)

    override fun storeStringArray(stringArray: Array<String>, name: PropertyName) =
        storeAsString(if (quoteString) stringArray.map { "\"$it\"" } else stringArray.map(Any::toString), name)
}