package io.config4k.ksp

class Schema(val name: String, val home: String, val visibility: Visibility) {
    enum class Visibility {PUBLIC, INTERNAL}

    val properties: MutableList<Property> = ArrayList()
}

class Property(val name: String, val propertyName: Array<String>, val type: PropertyType)
sealed class PropertyType {
    abstract val loadableType: LoadableType
}
sealed class LoadableType: PropertyType() {
    override val loadableType: LoadableType
        get() = this
}
data class ComplexType(val qn: String, val sn: String): LoadableType()
data class SimpleArrayType(val type: SimpleType): LoadableType()
data class SimpleType(val name: String): LoadableType() {
    companion object {
        val BYTE = SimpleType("Byte")
        val SHORT = SimpleType("Short")
        val INT = SimpleType("Int")
        val LONG = SimpleType("Long")
        val FLOAT = SimpleType("Float")
        val DOUBLE = SimpleType("Double")
        val U_BYTE = SimpleType("UByte")
        val U_SHORT = SimpleType("UShort")
        val U_INT = SimpleType("UInt")
        val U_LONG = SimpleType("ULong")
        val BOOLEAN = SimpleType("Boolean")
        val STRING = SimpleType("String")
    }
}

fun SimpleType.array() = SimpleArrayType(this)