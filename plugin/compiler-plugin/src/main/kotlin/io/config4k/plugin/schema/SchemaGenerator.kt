package io.config4k.plugin.schema

import io.config4k.annotation.Inlined
import io.config4k.annotation.Properties
import io.config4k.annotation.PropertiesConstructor
import io.config4k.annotation.PropertyName
import io.config4k.plugin.*
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.jvm.codegen.psiElement
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSourceLocation
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.cli.common.messages.MessageUtil
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.types.*
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.name.FqName

class SchemaGenerator(
    override val messageCollector: MessageCollector,
    private val context: Config4kContext,
    private val c4kBuiltIns: Config4kBuiltIns,
    private val pluginContext: IrPluginContext,
) : LoggingComponent {
    private data class LoaderMethods(val blocking: IrFunction?, val async: IrFunction?)

    @Suppress("NOTHING_TO_INLINE")
    sealed class SchemeValue {
        object NoScheme : SchemeValue()
        object SchemeError : SchemeValue()
        class ValidScheme(@JvmField val schema: Scheme) : SchemeValue()
        object SchemeComputationRunning : SchemeValue()

        inline fun isNoSchema(): Boolean = this is NoScheme
        inline fun isError(): Boolean = this is SchemeError
        inline fun schemaValue(): Scheme? = (this as? ValidScheme)?.schema
    }
    private fun Scheme.valid(): SchemeValue.ValidScheme = SchemeValue.ValidScheme(this)

    @OptIn(ExperimentalUnsignedTypes::class)
    companion object {
        private val propertiesConstructorName = PropertiesConstructor::class.fqName()!!
        private val inlinedName = Inlined::class.fqName()!!

        private val uByteArrayName = UByteArray::class.fqName()!!
        private val uShortArrayName = UShortArray::class.fqName()!!
        private val uIntArrayName = UIntArray::class.fqName()!!
        private val uLongArray = ULongArray::class.fqName()!!
        private val arrayName = Array::class.fqName()!!
    }

    private val computedSchemas = HashMap<String, SchemeValue>()
    val schemas get() = computedSchemas.values

    override val logInfo: Boolean
        get() = context.logInfo
    override val logDebug: Boolean
        get() = context.logDebug

    fun has(schemaQN: String): Boolean = schemaQN in computedSchemas
    fun getByName(name: String): SchemeValue? = computedSchemas[name]

    fun from(declaration: IrFile): List<SchemeValue> {
        return declaration.declarations.filterIsInstance<IrClass>().map { from(it) }
    }

    fun from(declaration: IrClass): SchemeValue {
        val name = declaration.kotlinFqName.asString()
        computedSchemas.whenPresent(name) {
            return it
        }
        computedSchemas[name] = SchemeValue.SchemeComputationRunning
        return makeSchemaFrom(declaration).also {
            computedSchemas[name] = it
        }

    }

    private fun IrClass.findFactory(): IrClass? {
        return declarations.firstOrNull {
            it is IrClass && it.isObject && it.name == c4kBuiltIns.factoryName
        } as? IrClass
    }

    /**
     * @receiver the factory
     */
    private fun IrClass.findLoaderMethods(): LoaderMethods {
        printVisible("find loader methods: ${functions.map { it.name }.joinToString()}")
        var loadBlocking: IrFunction? = null
        var loadAsync: IrFunction? = null
        for (function in functions) {
            if (function.name == c4kBuiltIns.loadName && function.valueParameters.size == 1) {
                when (function.valueParameters.first().fqNameWhenAvailable) {
                    c4kBuiltIns.blockingPropertyLoaderFqn -> {
                        loadBlocking = function
                        if (loadAsync != null) break
                    }
                    c4kBuiltIns.asyncPropertyLoaderFqn -> {
                        loadAsync = function
                        if (loadBlocking != null) break
                    }
                }
            }
        }
        return LoaderMethods(loadBlocking, loadAsync)
    }

    /**
     * @receiver properties class
     */
    private fun IrClass.findCompanionLoaderMethods(): LoaderMethods {
        val companionObject = companionObject()!! as IrClass
        return companionObject.findLoaderMethods()
    }


    private fun fromExternal(declaration: IrClass): Scheme? {
        val factory = declaration.findFactory() ?: return failLogging(
            CompilerMessageSeverity.ERROR,
            "incorrect property loading implementation of external class ${declaration.kotlinFqName}",
            MessageUtil.psiElementToMessageLocation(declaration.psiElement)
        )
        val (blocking, async) = factory.findLoaderMethods()
        return ExternalScheme(declaration, factory, blocking, async)
    }

    private fun makeSchemaFrom(irClass: IrClass): SchemeValue {
        val propertiesAnnotation: Properties = irClass.instantiateAnnotation() ?: return SchemeValue.NoScheme
        if (irClass.isExternal)
            return fromExternal(irClass)?.valid() ?: SchemeValue.SchemeError
        if (!validatePropertyAnnotation(irClass))
            return SchemeValue.SchemeError
        reportDebug1 {
            "creating scheme for ${irClass.kotlinFqName}" to irClass
        }
        val constructor = propertiesConstructorFrom(irClass) ?: return schemaError
        val schema = instantiateSchema(irClass, constructor, propertiesAnnotation) ?: return schemaError
        return if (!populateSchema(schema))
            SchemeValue.SchemeError
        else
            schema.valid()
    }

    private fun validatePropertyAnnotation(irClass: IrClass): Boolean {
        fun reportNoApplicable(type: String, advise: String? = null): Boolean {
            reportError(
                "The @Property annotation is not applicable to declarations of $type. ${advise.orEmpty()}",
                irClass
            )
            return false
        }

        return when {
            irClass.isInterface -> reportNoApplicable("interface")
            irClass.isEnumClass -> reportNoApplicable("enum")
            irClass.isInner -> reportNoApplicable("inner class")
            irClass.isInline -> reportNoApplicable(
                "inline",
                "Please consult the documentation on how to handle inline types"
            )
            irClass.isObject -> reportNoApplicable("object")
            irClass.modality == Modality.ABSTRACT -> reportNoApplicable("abstract class")
            irClass.modality == Modality.SEALED -> reportNoApplicable("sealed class")
            else -> true
        }
    }

    private fun instantiateSchema(irClass: IrClass, constructor: IrConstructor, properties: Properties): SourceScheme? {
        val factory = irClass.findFactory()!!
        val (blocking, async) = factory.findLoaderMethods()
        val (companionLoadBlocking, companionLoadAsync) = irClass.findCompanionLoaderMethods()
            return SourceScheme(
                propertiesClass = irClass,
                propertiesFactory = factory,
                propertiesConstructor = propertiesConstructorFrom(irClass) ?: return null,
                loadBlocking = blocking,
                loadAsync = async,
                companionLoadBlocking = companionLoadBlocking,
                companionLoadAsync = companionLoadAsync,
                properties.cascade,
            )
    }

    private fun propertiesConstructorFrom(irClass: IrClass): IrConstructor? {
        return irClass
            .constructors
            .firstOrNull { it.hasAnnotation(propertiesConstructorName) }
            ?: irClass.primaryConstructor
            ?: return failLogging(
                CompilerMessageSeverity.ERROR,
                ("A properties class must have a primary constructor or a constructor annotated with" +
                        " @${propertiesConstructorName.asString()}").trimMargin(),
                irClass.declarationLocation()
            )

    }

    /**
     * @return return false when an error occurred
     */
    private fun populateSchema(scheme: SourceScheme): Boolean {
        var isValid = true
        if (scheme.propertiesConstructor.valueParameters.isEmpty())
            reportWarning("empty properties constructor", scheme.propertiesConstructor)
        scheme.propertiesConstructor.valueParameters.forEach { valueParameter: IrValueParameter ->
            val type = valueParameter.type.run {
                val paramType = valueParameter.type
                when {
                    isByte() -> SimpleType.Byte
                    isShort() -> SimpleType.Short
                    isInt() -> SimpleType.Int
                    isLong() -> SimpleType.Long
                    isFloat() -> SimpleType.Float
                    isDouble() -> SimpleType.Double
                    isBoolean() -> SimpleType.Boolean
                    isString() -> SimpleType.String
                    isUByte() -> SimpleType.UByte
                    isUShort() -> SimpleType.UShort
                    isUInt() -> SimpleType.UInt
                    isULong() -> SimpleType.ULong
                    isByteArray() -> SimpleType.Byte.array
                    isShortArray() -> SimpleType.Short.array
                    isIntArray() -> SimpleType.Int.array
                    isLongArray() -> SimpleType.Long.array
                    isFloatArray() -> SimpleType.Float.array
                    isDoubleArray() -> SimpleType.Double.array
                    isBooleanArray() -> SimpleType.Boolean.array
                    paramType is IrSimpleType && paramType.classifier is IrClassSymbol -> {
                        val classifier = paramType.classifier as IrClassSymbol
                        when (classifier.owner.kotlinFqName) {
                            uByteArrayName -> SimpleType.UByte.array
                            uShortArrayName -> SimpleType.UShort.array
                            uIntArrayName -> SimpleType.UInt.array
                            uLongArray -> SimpleType.ULong.array
                            arrayName -> {
                                if (paramType.arguments.first().typeOrNull.nullOnFalseOrElse { t -> t.isString() })
                                    SimpleType.String.array
                                else {
                                    val typeParamName =
                                        paramType.arguments.first().typeOrNull?.getClass()?.kotlinFqName?.asString()
                                    reportError(
                                        "invalid array type $typeParamName in parameter with name ${valueParameter.name.identifier} " +
                                                "in class ${scheme.fullName}, only the String type is allowed, " +
                                                "for primitive types please use their respective types like ByteArray",
                                        valueParameter
                                    )
                                    isValid = false
                                    return@forEach
                                }
                            }
                            else -> {
                                val valueClass = valueParameter.type.getClass() ?: kotlin.run {
                                    invalidPropertyType(null, valueParameter)
                                    isValid = false
                                    return@forEach
                                }
                                when (from(valueClass)) {
                                    is SchemeValue.SchemeError -> {
                                        isValid = false
                                        messageCollector.reportError(
                                            "properties class ${scheme.fullName} depends on erroneous class "
                                                    + valueClass.name,
                                            valueParameter
                                        )
                                        return@forEach
                                    }
                                    is SchemeValue.ValidScheme -> {
                                        ComplexType(scheme.propertiesClass, scheme.propertiesConstructor)
                                    }
                                    is SchemeValue.NoScheme -> {
                                        invalidPropertyType(valueClass.kotlinFqName, valueParameter)
                                        isValid = false
                                        messageCollector.reportError(
                                            "properties class ${scheme.fullName} depends on class " +
                                                    "${valueClass.name} which is no properties class ",
                                            valueParameter
                                        )
                                        return@forEach
                                    }
                                    is SchemeValue.SchemeComputationRunning -> {
                                        reportError(
                                            "circular dependency with ${valueClass.kotlinFqName.asString()}",
                                            valueParameter
                                        )
                                        isValid = false
                                        return@forEach
                                    }
                                }
                            }
                        }
                    }
                    else -> {
                        invalidPropertyType(null, valueParameter)
                        return@forEach
                    }
                }
            }
            val propertyName =
                valueParameter.instantiateAnnotation<PropertyName>()?.nameElements?.toList() ?: splitName(valueParameter.name.identifier)
            val inlined = valueParameter.hasAnnotation(inlinedName)
            scheme.addProperty(valueParameter.name.identifier, propertyName, valueParameter.index, type, inlined)
        }
        return isValid
    }

    private fun splitName(name: String): List<String> {
        val split = ArrayList<String>(2)
        var lastSplit = 0
        for ((i, char) in name.withIndex()) {
            if (char.isUpperCase()) {
                val splitPiece = name.substring(lastSplit, i)
                if (splitPiece.isNotEmpty()) split.add(splitPiece.decapitalize())
                lastSplit = i
            }
        }
        split.add(name.substring(lastSplit, name.length).decapitalize())
        return split
    }

    private fun <T : Any> failLogging(
        severity: CompilerMessageSeverity,
        message: String,
        location: CompilerMessageSourceLocation? = null
    ): T? {
        messageCollector.report(severity, message, location)
        return null
    }

    private fun invalidPropertyType(propertyType: FqName?, at: IrDeclaration) {
        reportError(
            "invalid type ${propertyType?.asString() ?: ""} in properties constructor, " +
                    "please consult the documentation for valid types", at
        )
    }

    private inline val schemaError get() = SchemaGenerator.SchemeValue.SchemeError
}