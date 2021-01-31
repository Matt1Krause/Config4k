package io.config4k.plugin.schema

import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrConstructor
import org.jetbrains.kotlin.ir.types.IrType

data class Property(
    @JvmField val fieldName: String,
    @JvmField val propertyName: List<String>,
    @JvmField val index: Int,
    @JvmField val type: PropertyType,
    @JvmField val inlined: Boolean,
)
sealed class PropertyType {
    abstract val  asLoadableType: LoadableType
}
sealed class LoadableType: PropertyType() {
    final override val asLoadableType: LoadableType
        get() = this
}
class ComplexType(
    @JvmField val irType: IrClass,
    @JvmField val irConstructor: IrConstructor
    ): LoadableType()

data class ArrayType(@JvmField val arrayType: SimpleType): LoadableType()

sealed class SimpleType: LoadableType() {
    object Byte: SimpleType()
    object Short: SimpleType()
    object Int: SimpleType()
    object Long: SimpleType()
    object Float: SimpleType()
    object Double: SimpleType()
    object Boolean: SimpleType()
    object String: SimpleType()
    object UByte: SimpleType()
    object UShort: SimpleType()
    object UInt: SimpleType()
    object ULong: SimpleType()
}

inline val SimpleType.array: ArrayType get() = ArrayType(this)
