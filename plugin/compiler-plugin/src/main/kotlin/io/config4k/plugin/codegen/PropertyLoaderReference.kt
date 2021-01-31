package io.config4k.plugin.codegen

import io.config4k.blocking.PropertyLoader
import io.config4k.plugin.referenceClass
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.util.functions

class PropertyLoaderReference(pluginContext: IrPluginContext) {
    @JvmField val propertyLoaderClass: IrClassSymbol = pluginContext.referenceClass<PropertyLoader>()
    @JvmField val asyncPropertyLoaderClass: IrClassSymbol = pluginContext.referenceClass<PropertyLoader>()
    @JvmField val propertyLoaderType: IrType = propertyLoaderClass.defaultType
    @JvmField val asyncPropertyLoaderType: IrType = asyncPropertyLoaderClass.defaultType

    @JvmField val blockingLoadByte: IrFunction
    @JvmField val blockingLoadShort: IrFunction
    @JvmField val blockingLoadInt: IrFunction
    @JvmField val blockingLoadLong: IrFunction
    @JvmField val blockingLoadFloat: IrFunction
    @JvmField val blockingLoadDouble: IrFunction
    @JvmField val blockingLoadBoolean: IrFunction
    @JvmField val blockingLoadString: IrFunction
    @JvmField val blockingLoadUByte: IrFunction
    @JvmField val blockingLoadUShort: IrFunction
    @JvmField val blockingLoadUInt: IrFunction
    @JvmField val blockingLoadULong: IrFunction

    @JvmField val blockingLoadByteArray: IrFunction
    @JvmField val blockingLoadShortArray: IrFunction
    @JvmField val blockingLoadIntArray: IrFunction
    @JvmField val blockingLoadLongArray: IrFunction
    @JvmField val blockingLoadFloatArray: IrFunction
    @JvmField val blockingLoadDoubleArray: IrFunction
    @JvmField val blockingLoadBooleanArray: IrFunction
    @JvmField val blockingLoadStringArray: IrFunction
    @JvmField val blockingLoadUByteArray: IrFunction
    @JvmField val blockingLoadUShortArray: IrFunction
    @JvmField val blockingLoadUIntArray: IrFunction
    @JvmField val blockingLoadULongArray: IrFunction

    @JvmField val asyncLoadByte: IrFunction
    @JvmField val asyncLoadShort: IrFunction
    @JvmField val asyncLoadInt: IrFunction
    @JvmField val asyncLoadLong: IrFunction
    @JvmField val asyncLoadFloat: IrFunction
    @JvmField val asyncLoadDouble: IrFunction
    @JvmField val asyncLoadBoolean: IrFunction
    @JvmField val asyncLoadString: IrFunction
    @JvmField val asyncLoadUByte: IrFunction
    @JvmField val asyncLoadUShort: IrFunction
    @JvmField val asyncLoadUInt: IrFunction
    @JvmField val asyncLoadULong: IrFunction

    @JvmField val asyncLoadByteArray: IrFunction
    @JvmField val asyncLoadShortArray: IrFunction
    @JvmField val asyncLoadIntArray: IrFunction
    @JvmField val asyncLoadLongArray: IrFunction
    @JvmField val asyncLoadFloatArray: IrFunction
    @JvmField val asyncLoadDoubleArray: IrFunction
    @JvmField val asyncLoadBooleanArray: IrFunction
    @JvmField val asyncLoadStringArray: IrFunction
    @JvmField val asyncLoadUByteArray: IrFunction
    @JvmField val asyncLoadUShortArray: IrFunction
    @JvmField val asyncLoadUIntArray: IrFunction
    @JvmField val asyncLoadULongArray: IrFunction

    init {
        val blockingMethods = propertyLoaderClass.functions
        val asyncMethods = asyncPropertyLoaderClass.functions

        var blockingLoadByte: IrFunction? = null
        var blockingLoadShort: IrFunction? = null
        var blockingLoadInt: IrFunction? = null
        var blockingLoadLong: IrFunction? = null
        var blockingLoadFloat: IrFunction? = null
        var blockingLoadDouble: IrFunction? = null
        var blockingLoadBoolean: IrFunction? = null
        var blockingLoadString: IrFunction? = null
        var blockingLoadUByte: IrFunction? = null
        var blockingLoadUShort: IrFunction? = null
        var blockingLoadUInt: IrFunction? = null
        var blockingLoadULong: IrFunction? = null

        var blockingLoadByteArray: IrFunction? = null
        var blockingLoadShortArray: IrFunction? = null
        var blockingLoadIntArray: IrFunction? = null
        var blockingLoadLongArray: IrFunction? = null
        var blockingLoadFloatArray: IrFunction? = null
        var blockingLoadDoubleArray: IrFunction? = null
        var blockingLoadBooleanArray: IrFunction? = null
        var blockingLoadStringArray: IrFunction? = null
        var blockingLoadUByteArray: IrFunction? = null
        var blockingLoadUShortArray: IrFunction? = null
        var blockingLoadUIntArray: IrFunction? = null
        var blockingLoadULongArray: IrFunction? = null

        var asyncLoadByte: IrFunction? = null
        var asyncLoadShort: IrFunction? = null
        var asyncLoadInt: IrFunction? = null
        var asyncLoadLong: IrFunction? = null
        var asyncLoadFloat: IrFunction? = null
        var asyncLoadDouble: IrFunction? = null
        var asyncLoadBoolean: IrFunction? = null
        var asyncLoadString: IrFunction? = null
        var asyncLoadUByte: IrFunction? = null
        var asyncLoadUShort: IrFunction? = null
        var asyncLoadUInt: IrFunction? = null
        var asyncLoadULong: IrFunction? = null

        var asyncLoadByteArray: IrFunction? = null
        var asyncLoadShortArray: IrFunction? = null
        var asyncLoadIntArray: IrFunction? = null
        var asyncLoadLongArray: IrFunction? = null
        var asyncLoadFloatArray: IrFunction? = null
        var asyncLoadDoubleArray: IrFunction? = null
        var asyncLoadBooleanArray: IrFunction? = null
        var asyncLoadStringArray: IrFunction? = null
        var asyncLoadUByteArray: IrFunction? = null
        var asyncLoadUShortArray: IrFunction? = null
        var asyncLoadUIntArray: IrFunction? = null
        var asyncLoadULongArray: IrFunction? = null

        /*
         *  types = ["Byte", "Short", "Int", "Long", "Float", "Double", "Boolean", "String", "UByte", "UShort", "UInt", "ULong"]
            blockingWord = "async"
            arrayWord = "Array"
            for type in types:
                print(blockingWord + "Load" + type + arrayWord + " == null && name == \"load" + type + arrayWord + "\" -> " + blockingWord + "Load" + type + arrayWord + " = function.owner")
         */

        for (function in blockingMethods) {
            val name = function.owner.name.asString()
            when {
                blockingLoadByte == null && name == "loadByte" -> blockingLoadByte = function.owner
                blockingLoadShort == null && name == "loadShort" -> blockingLoadShort = function.owner
                blockingLoadInt == null && name == "loadInt" -> blockingLoadInt = function.owner
                blockingLoadLong == null && name == "loadLong" -> blockingLoadLong = function.owner
                blockingLoadFloat == null && name == "loadFloat" -> blockingLoadFloat = function.owner
                blockingLoadDouble == null && name == "loadDouble" -> blockingLoadDouble = function.owner
                blockingLoadBoolean == null && name == "loadBoolean" -> blockingLoadBoolean = function.owner
                blockingLoadString == null && name == "loadString" -> blockingLoadString = function.owner
                blockingLoadUByte == null && name == "loadUByte" -> blockingLoadUByte = function.owner
                blockingLoadUShort == null && name == "loadUShort" -> blockingLoadUShort = function.owner
                blockingLoadUInt == null && name == "loadUInt" -> blockingLoadUInt = function.owner
                blockingLoadULong == null && name == "loadULong" -> blockingLoadULong = function.owner

                blockingLoadByteArray == null && name == "loadByteArray" -> blockingLoadByteArray = function.owner
                blockingLoadShortArray == null && name == "loadShortArray" -> blockingLoadShortArray = function.owner
                blockingLoadIntArray == null && name == "loadIntArray" -> blockingLoadIntArray = function.owner
                blockingLoadLongArray == null && name == "loadLongArray" -> blockingLoadLongArray = function.owner
                blockingLoadFloatArray == null && name == "loadFloatArray" -> blockingLoadFloatArray = function.owner
                blockingLoadDoubleArray == null && name == "loadDoubleArray" -> blockingLoadDoubleArray = function.owner
                blockingLoadBooleanArray == null && name == "loadBooleanArray" -> blockingLoadBooleanArray = function.owner
                blockingLoadStringArray == null && name == "loadStringArray" -> blockingLoadStringArray = function.owner
                blockingLoadUByteArray == null && name == "loadUByteArray" -> blockingLoadUByteArray = function.owner
                blockingLoadUShortArray == null && name == "loadUShortArray" -> blockingLoadUShortArray = function.owner
                blockingLoadUIntArray == null && name == "loadUIntArray" -> blockingLoadUIntArray = function.owner
                blockingLoadULongArray == null && name == "loadULongArray" -> blockingLoadULongArray = function.owner
            }
        }

        for (function in asyncMethods) {
            val name = function.owner.name.asString()
            when {
                asyncLoadByte == null && name == "loadByte" -> asyncLoadByte = function.owner
                asyncLoadShort == null && name == "loadShort" -> asyncLoadShort = function.owner
                asyncLoadInt == null && name == "loadInt" -> asyncLoadInt = function.owner
                asyncLoadLong == null && name == "loadLong" -> asyncLoadLong = function.owner
                asyncLoadFloat == null && name == "loadFloat" -> asyncLoadFloat = function.owner
                asyncLoadDouble == null && name == "loadDouble" -> asyncLoadDouble = function.owner
                asyncLoadBoolean == null && name == "loadBoolean" -> asyncLoadBoolean = function.owner
                asyncLoadString == null && name == "loadString" -> asyncLoadString = function.owner
                asyncLoadUByte == null && name == "loadUByte" -> asyncLoadUByte = function.owner
                asyncLoadUShort == null && name == "loadUShort" -> asyncLoadUShort = function.owner
                asyncLoadUInt == null && name == "loadUInt" -> asyncLoadUInt = function.owner
                asyncLoadULong == null && name == "loadULong" -> asyncLoadULong = function.owner

                asyncLoadByteArray == null && name == "loadByteArray" -> asyncLoadByteArray = function.owner
                asyncLoadShortArray == null && name == "loadShortArray" -> asyncLoadShortArray = function.owner
                asyncLoadIntArray == null && name == "loadIntArray" -> asyncLoadIntArray = function.owner
                asyncLoadLongArray == null && name == "loadLongArray" -> asyncLoadLongArray = function.owner
                asyncLoadFloatArray == null && name == "loadFloatArray" -> asyncLoadFloatArray = function.owner
                asyncLoadDoubleArray == null && name == "loadDoubleArray" -> asyncLoadDoubleArray = function.owner
                asyncLoadBooleanArray == null && name == "loadBooleanArray" -> asyncLoadBooleanArray = function.owner
                asyncLoadStringArray == null && name == "loadStringArray" -> asyncLoadStringArray = function.owner
                asyncLoadUByteArray == null && name == "loadUByteArray" -> asyncLoadUByteArray = function.owner
                asyncLoadUShortArray == null && name == "loadUShortArray" -> asyncLoadUShortArray = function.owner
                asyncLoadUIntArray == null && name == "loadUIntArray" -> asyncLoadUIntArray = function.owner
                asyncLoadULongArray == null && name == "loadULongArray" -> asyncLoadULongArray = function.owner
            }
        }

        this.blockingLoadByte = blockingLoadByte!!
        this.blockingLoadShort = blockingLoadShort!!
        this.blockingLoadInt = blockingLoadInt!!
        this.blockingLoadLong = blockingLoadLong!!
        this.blockingLoadFloat = blockingLoadFloat!!
        this.blockingLoadDouble = blockingLoadDouble!!
        this.blockingLoadBoolean = blockingLoadBoolean!!
        this.blockingLoadString = blockingLoadString!!
        this.blockingLoadUByte = blockingLoadUByte!!
        this.blockingLoadUShort = blockingLoadUShort!!
        this.blockingLoadUInt = blockingLoadUInt!!
        this.blockingLoadULong = blockingLoadULong!!

        this.blockingLoadByteArray = blockingLoadByteArray!!
        this.blockingLoadShortArray = blockingLoadShortArray!!
        this.blockingLoadIntArray = blockingLoadIntArray!!
        this.blockingLoadLongArray = blockingLoadLongArray!!
        this.blockingLoadFloatArray = blockingLoadFloatArray!!
        this.blockingLoadDoubleArray = blockingLoadDoubleArray!!
        this.blockingLoadBooleanArray = blockingLoadBooleanArray!!
        this.blockingLoadStringArray = blockingLoadStringArray!!
        this.blockingLoadUByteArray = blockingLoadUByteArray!!
        this.blockingLoadUShortArray = blockingLoadUShortArray!!
        this.blockingLoadUIntArray = blockingLoadUIntArray!!
        this.blockingLoadULongArray = blockingLoadULongArray!!

        this.asyncLoadByte = asyncLoadByte!!
        this.asyncLoadShort = asyncLoadShort!!
        this.asyncLoadInt = asyncLoadInt!!
        this.asyncLoadLong = asyncLoadLong!!
        this.asyncLoadFloat = asyncLoadFloat!!
        this.asyncLoadDouble = asyncLoadDouble!!
        this.asyncLoadBoolean = asyncLoadBoolean!!
        this.asyncLoadString = asyncLoadString!!
        this.asyncLoadUByte = asyncLoadUByte!!
        this.asyncLoadUShort = asyncLoadUShort!!
        this.asyncLoadUInt = asyncLoadUInt!!
        this.asyncLoadULong = asyncLoadULong!!

        this.asyncLoadByteArray = asyncLoadByteArray!!
        this.asyncLoadShortArray = asyncLoadShortArray!!
        this.asyncLoadIntArray = asyncLoadIntArray!!
        this.asyncLoadLongArray = asyncLoadLongArray!!
        this.asyncLoadFloatArray = asyncLoadFloatArray!!
        this.asyncLoadDoubleArray = asyncLoadDoubleArray!!
        this.asyncLoadBooleanArray = asyncLoadBooleanArray!!
        this.asyncLoadStringArray = asyncLoadStringArray!!
        this.asyncLoadUByteArray = asyncLoadUByteArray!!
        this.asyncLoadUShortArray = asyncLoadUShortArray!!
        this.asyncLoadUIntArray = asyncLoadUIntArray!!
        this.asyncLoadULongArray = asyncLoadULongArray!!
    }

}