package io.config4k.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import io.config4k.async.AsyncPropertyLoader
import io.config4k.blocking.PropertyLoader
import java.io.PrintWriter
import kotlin.reflect.KClass

class CodeGen(private val schema: Schema, codeGenerator: CodeGenerator, private val env: GenerationEnv) {
    private val file: PrintWriter
    private val basePackage = "io.config4k"

    init {
        val fileName = "${schema.name}Util"
        val packageName = schema.home.let {
            when {
                it.isBlank() -> "generated"
                env.alwaysGeneratedFolder -> "$it.generated"
                else -> it
            }
        }
        file = PrintWriter(codeGenerator.createNewFile(packageName, fileName))
        section {
            emitLine("@file:JvmName(\"$fileName\")")
            emitLine("package $packageName")
        }
        section {
            importSection()
        }
        section {
            rawConstructorSection()
        }
        section {
            constructorSection(false)
        }
        section {
            constructorSection(true)
        }
        file.close()
    }

    private fun emitLine(line: String) {
        file.println(line)
    }
    private inline fun section(block: ()->Unit) {
        block()
        file.println()
        file.flush()
    }

    private fun functionDecl(visibility: String, mods: List<String>, name: String) {
        val modStr = mods.joinToString(separator = " ").let { if(it.isNotBlank()) " $it" else "" }
        emitLine("$visibility$modStr fun $name(")
    }

    private fun parameter(name: String, type: String) = emitLine("\t$name: $type, ")
    private fun namedArgument(name: String, value: String) = emitLine("\t$name = $value,")

    private fun Array<String>.toCodeArray(): String {
        return joinToString(prefix = "arrayOf(", postfix = ")") { "\"$it\"" }
    }

    private fun Schema.Visibility.toMod(): String = when (this) {
        Schema.Visibility.PUBLIC -> "public"
        Schema.Visibility.INTERNAL -> "internal"
    }

    private fun importSection() {
        fun import(fullName: String) = emitLine("import $fullName")
        fun import(klass: KClass<*>) = import(klass.qualifiedName!!)
        fun importConfig4k(name: String) = import("$basePackage.$name")

        import(PropertyLoader::class)
        import(AsyncPropertyLoader::class)
        importConfig4k("named")
        importConfig4k("asyncNamed")

        for (property in schema.properties)
            when (val type = property.type) {
                is ComplexType -> import(type.qn)
            }
    }

    private fun rawConstructorSection() {
        functionDecl("private", emptyList(), schema.name + "Raw")
        for (property in schema.properties) {
            val typeName = when (val type = property.type.loadableType) {
                is SimpleArrayType -> {
                    if (type.type == SimpleType.STRING)
                        "Array<String>"
                    else
                        "${type.type.name}Array"
                }
                is SimpleType -> type.name
                is ComplexType -> type.qn
            }
            parameter(property.name, typeName)
        }
        emitLine("): ${schema.name} = ${schema.name}(")
        for (property in schema.properties) {
            if (property.type is LoadableType) {
                namedArgument(property.name, property.name)
            } else {
                error("unhandled type")
            }
        }
        emitLine(")")
    }

    private fun constructorSection(async: Boolean) {
        val propertyLoaderType = if (async) "AsyncPropertyLoader" else "PropertyLoader"
        val modList = ArrayList<String>(1)
        if (async) modList.add("suspend")
        val namedMethod = if (async) "asyncNamed" else "named"
        functionDecl(schema.visibility.toMod(), modList, schema.name)
        parameter("propertyLoader", propertyLoaderType)
        emitLine("): ${schema.name} = ${schema.name}Raw(")
        for (property in schema.properties) {
            val nameAsArray = property.propertyName.toCodeArray()
            val method = when (val type = property.type.loadableType) {
                is ComplexType -> "${type.sn}(propertyLoader.$namedMethod($nameAsArray))"
                is SimpleType -> "propertyLoader.load${type.name}($nameAsArray)"
                is SimpleArrayType -> "propertyLoader.load${type.type.name}Array($nameAsArray)"
            }
            namedArgument(property.name, method)
        }
        emitLine(")")
    }
}

