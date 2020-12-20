package io.config4k.ksp

import com.google.devtools.ksp.getConstructors
import com.google.devtools.ksp.getVisibility
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.*
import io.config4k.annotation.Properties
import io.config4k.annotation.PropertiesConstructor
import io.config4k.annotation.PropertyName

fun generateSchemaFrom(klass: KSClassDeclaration, logger: KSPLogger): Schema? {
    val schema = instantiateSchemaFrom(klass, logger) ?: return null
    val constructor: KSFunctionDeclaration = klass.getConstructors().firstOrNull {
        it.annotation<PropertiesConstructor>() != null
    } ?: klass.primaryConstructor ?: run {
        logger.error("a properties class must have a constructor", klass)
        return null
    }
    populateSchemaFromFunction(constructor, schema)
    return schema
}

private fun instantiateSchemaFrom(klass: KSClassDeclaration, logger: KSPLogger): Schema? {
    val name = klass.simpleName.asString()
    val home = klass.packageName.asString()
    val properties = klass.propertiesAnnotation!!
    val visibility: Schema.Visibility = properties.parameter("forceInternal")
        ?.let { if (it.value == true) Schema.Visibility.INTERNAL else null }
        ?: klass.getVisibility().let {
            when (it) {
                Visibility.INTERNAL -> Schema.Visibility.INTERNAL
                Visibility.PUBLIC -> Schema.Visibility.PUBLIC
                else -> {
                    logger.error("invalid visibility: $it, only public and internal are accepted", klass)
                    return null
                }
            }
        }
    return Schema(name = name, home = home, visibility = visibility)
}

private fun populateSchemaFromFunction(function: KSFunctionDeclaration, schema: Schema) {
    function.parameters.forEach {
        val typeDecl = it.type.resolve().declaration
        val propertyType: PropertyType =
            typeDecl.qualifiedName?.asString()?.let { qn -> getLoadableTypeForQN(qn, it) } ?: run {
                if (typeDecl.propertiesAnnotation != null) {
                    ComplexType(typeDecl.qualifiedName?.asString() ?: typeDecl.simpleName.asString(), typeDecl.simpleName.asString())
                } else {
                    error("unhandled type")
                }
            }
        val propertyName = it.annotation<PropertyName>()?.parameter("nameElements")?.let { value ->
            @Suppress("UNCHECKED_CAST")
            value.value as List<String>
        }?: splitPropertyName(it.name?.asString()!!)
        schema.properties.add(Property(it.name!!.asString(), propertyName.toTypedArray(), propertyType))
    }
}

private fun getLoadableTypeForQN(qualifiedName: String, ref: KSValueParameter): LoadableType? {
    if (!qualifiedName.startsWith("kotlin")) return null
    return when (qualifiedName.substring(7 until qualifiedName.length)) {
        "Byte" -> SimpleType.BYTE
        "Short" -> SimpleType.SHORT
        "Int" -> SimpleType.INT
        "Long" -> SimpleType.LONG
        "Float" -> SimpleType.FLOAT
        "Double" -> SimpleType.DOUBLE
        "UByte" -> SimpleType.U_BYTE
        "UShort" -> SimpleType.U_SHORT
        "UInt" -> SimpleType.U_INT
        "ULong" -> SimpleType.U_LONG
        "Boolean" -> SimpleType.BOOLEAN
        "String" -> SimpleType.STRING
        "ByteArray" -> SimpleType.BYTE.array()
        "ShortArray" -> SimpleType.SHORT.array()
        "IntArray" -> SimpleType.INT.array()
        "LongArray" -> SimpleType.LONG.array()
        "FloatArray" -> SimpleType.FLOAT.array()
        "DoubleArray" -> SimpleType.DOUBLE.array()
        "UByteArray" -> SimpleType.U_BYTE.array()
        "UShortArray" -> SimpleType.U_SHORT.array()
        "UIntArray" -> SimpleType.U_INT.array()
        "ULongArray" -> SimpleType.U_LONG.array()
        "BooleanArray" -> SimpleType.BOOLEAN.array()
        "Array" -> {
            val isSA = ref.type.resolve().arguments.first()
                .type?.resolve()?.declaration?.qualifiedName?.asString() == "kotlin.String"
            if (isSA) SimpleType.STRING.array()
            else null
        }
        else -> null
    }
}

private fun KSAnnotated.annotation(qualifiedName: String): KSAnnotation? = annotations.firstOrNull {
    it.annotationType.resolve().declaration.qualifiedName?.asString() == qualifiedName
}

private inline fun <reified T: Annotation> KSAnnotated.annotation() = annotation(T::class.qualifiedName!!)
private fun KSAnnotation.parameter(name: String) = arguments.firstOrNull { it.name?.asString() == name }

private val KSAnnotated.propertiesAnnotation: KSAnnotation?
    get() = annotation<Properties>()

private fun splitPropertyName(name: String): List<String> {
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