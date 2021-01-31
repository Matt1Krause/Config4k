package io.config4k.plugin.codegen

import io.config4k.plugin.Config4kContext
import io.config4k.plugin.referenceKotlinClass
import io.config4k.plugin.schema.*
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.ir.addChild
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.descriptors.ValueParameterDescriptor
import org.jetbrains.kotlin.ir.builders.*
import org.jetbrains.kotlin.ir.builders.declarations.IrDeclarationBuilder
import org.jetbrains.kotlin.ir.builders.declarations.addValueParameter
import org.jetbrains.kotlin.ir.builders.declarations.buildFun
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.IrConst
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrFunctionAccessExpression
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.name.Name
import kotlin.reflect.jvm.internal.impl.descriptors.impl.ValueParameterDescriptorImpl

class CodeGeneration(
    private val config4kContext: Config4kContext,
    private val pluginContext: IrPluginContext,
    generateFrom: List<Scheme>
) {
    companion object {
        private val propertyLoaderParameterName = Name.identifier("propertyLoader")
    }

    private object PropertiesBaseConstructorOrigin :
        IrDeclarationOriginImpl("PROPERTIES_BASE_CONSTRUCTOR", true)

    private object PropertiesConstructorOrigin : IrDeclarationOriginImpl("PROPERTIES_CONSTRUCTOR", false)

    fun generate(from: SourceScheme) {
        val file = from.propertiesClass.file
        val rawConstructor = generateRawConstructor(file, from)
        generateConstructors(file, from, rawConstructor)
    }

    private val builtIns = pluginContext.irBuiltIns

    private val stringArrayType = pluginContext.symbols.array.typeWith(pluginContext.symbols.string.defaultType)
    private val booleanType = pluginContext.referenceKotlinClass("Boolean").defaultType
    private val uByteArrayType = pluginContext.referenceKotlinClass("UByteArray").defaultType
    private val uShortArrayType = pluginContext.referenceKotlinClass("UShortArray").defaultType
    private val uIntArrayType = pluginContext.referenceKotlinClass("UIntArray").defaultType
    private val uLongArrayType = pluginContext.referenceKotlinClass("ULongArray").defaultType
    val plr = PropertyLoaderReference(pluginContext)

    private fun generateRawConstructor(inFile: IrFile, scheme: SourceScheme): IrFunction {
        val theFun = scheme.propertiesFactory.factory.buildFun {
            name = Name.identifier("rawConstructor")
            visibility = DescriptorVisibilities.PRIVATE
            returnType = scheme.propertiesClass.typeWith()
            origin = PropertiesBaseConstructorOrigin
            originalDeclaration = scheme.propertiesConstructor
        }

        val parameters = scheme.properties.map { property ->
            theFun.addValueParameter {
                index = property.index
                name = Name.identifier(property.fieldName)
                type = loadableTypeToIrType(property.type.asLoadableType)
            } to property.index
        }
        theFun.body = DeclarationIrBuilder(pluginContext, theFun.symbol).irBlockBody {
            val constructorCall = irCall(scheme.propertiesConstructor).also { call ->
                for ((parameter, index) in parameters) {
                    call.putValueArgument(index, irGet(parameter))
                }
            }
            +irReturn(constructorCall)

        }
        inFile.addChild(theFun)
        return theFun
    }

    private fun generateConstructors(
        inFile: IrFile,
        scheme: SourceScheme,
        rawConstructor: IrFunction,
    ) {
        val blocking = scheme.loadBlocking
        val suspend = scheme.loadAsync

        val blockingLoader = blocking?.valueParameters?.first { it.name == propertyLoaderParameterName }
        val asyncLoader = suspend?.valueParameters?.first { it.name == propertyLoaderParameterName }
        val blockingBody = blocking?.let { DeclarationIrBuilder(pluginContext, it.symbol) }
        val blockingCall = blockingBody?.irCall(rawConstructor)
        val suspendBody = suspend?.let { DeclarationIrBuilder(pluginContext, it.symbol) }
        val suspendCall = suspendBody?.irCall(rawConstructor)
        for (property in scheme.properties) {
            when (property.type) {
                is ComplexType -> {
                    TODO()
                }
                is ArrayType -> TODO()
                SimpleType.Byte -> {
                    blockingCall?.argFromLoad(blockingBody, blockingLoader!!, plr.blockingLoadByte, property)
                    suspendCall?.argFromLoad(suspendBody, asyncLoader!!, plr.asyncLoadByte, property)
                }
                SimpleType.Short -> TODO()
                SimpleType.Int -> TODO()
                SimpleType.Long -> TODO()
                SimpleType.Float -> TODO()
                SimpleType.Double -> TODO()
                SimpleType.Boolean -> TODO()
                SimpleType.String -> TODO()
                SimpleType.UByte -> TODO()
                SimpleType.UShort -> TODO()
                SimpleType.UInt -> TODO()
                SimpleType.ULong -> TODO()
            }
        }
        blocking?.body = blockingBody?.irBlockBody { +blockingCall!! }
        suspend?.body = suspendBody?.irBlockBody { +suspendCall!! }
        blocking?.let { inFile.addChild(it) }
        suspend?.let { inFile.addChild(it) }
    }

    private fun loadableTypeToIrType(loadableType: LoadableType): IrType {
        return when (loadableType) {
            is ComplexType -> loadableType.irType.defaultType
            is SimpleType -> when (loadableType) {
                SimpleType.Byte -> pluginContext.symbols.byte.defaultType
                SimpleType.Short -> pluginContext.symbols.short.defaultType
                SimpleType.Int -> pluginContext.symbols.int.defaultType
                SimpleType.Long -> pluginContext.symbols.long.defaultType
                SimpleType.Float -> pluginContext.symbols.float.defaultType
                SimpleType.Double -> pluginContext.symbols.double.defaultType
                SimpleType.Boolean -> booleanType
                SimpleType.String -> pluginContext.symbols.string.defaultType
                SimpleType.UByte -> pluginContext.symbols.uByte!!.defaultType
                SimpleType.UShort -> pluginContext.symbols.uShort!!.defaultType
                SimpleType.UInt -> pluginContext.symbols.uInt!!.defaultType
                SimpleType.ULong -> pluginContext.symbols.uLong!!.defaultType
            }
            is ArrayType -> when (loadableType.arrayType) {
                SimpleType.Byte -> pluginContext.symbols.byteArray.defaultType
                SimpleType.Short -> pluginContext.symbols.shortArray.defaultType
                SimpleType.Int -> pluginContext.symbols.intArray.defaultType
                SimpleType.Long -> pluginContext.symbols.longArray.defaultType
                SimpleType.Float -> pluginContext.symbols.floatArray.defaultType
                SimpleType.Double -> pluginContext.symbols.doubleArray.defaultType
                SimpleType.Boolean -> pluginContext.symbols.booleanArray.defaultType
                SimpleType.String -> stringArrayType
                SimpleType.UByte -> uByteArrayType
                SimpleType.UShort -> uShortArrayType
                SimpleType.UInt -> uIntArrayType
                SimpleType.ULong -> uLongArrayType
            }
        }
    }

    private fun DeclarationIrBuilder.buildLoad(
        propertyLoader: IrValueParameter,
        function: IrFunction,
        property: Property
    ): IrExpression {
        return irCall(function).also {
            it.dispatchReceiver = irGet(propertyLoader)
            val arrayOfCall = irCall(pluginContext.symbols.arrayOf)
            for ((i, part) in property.propertyName.withIndex()) {
                arrayOfCall.putValueArgument(i, irString(part))
            }
            it.putValueArgument(0, arrayOfCall)
        }
    }

    private fun IrFunctionAccessExpression.argFromLoad(
        decl: DeclarationIrBuilder,
        propertyLoader: IrValueParameter,
        load: IrFunction,
        property: Property
    ) {
        putValueArgument(property.index, decl.buildLoad(propertyLoader, load, property))
    }
}