package io.config4k.plugin

import com.tschuchort.compiletesting.KotlinCompilation
import io.config4k.annotation.GenerationStance
import io.config4k.annotation.Properties
import io.config4k.async.AsyncPropertyLoader
import io.config4k.blocking.PropertyLoader
import java.io.FileDescriptor
import java.io.FileOutputStream
import java.io.PrintStream
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.declaredFunctions

class CompileTest : ClassContainer {
    val imports = Imports(this)
    val fileAnnotations = FileAnnotations(this)

    val classes: MutableList<CompileClass> = ArrayList()

    override fun add(klass: CompileClass) {
        classes.add(klass)
    }

    override val compileTest: CompileTest
        get() = this

    override fun toString(): String {
        return buildString {
            append(fileAnnotations)
            append(imports)
            for (klass in classes) {
                append(klass)
                append('\n')
            }
        }
    }
}

inline fun compileTest(block: CompileTest.() -> Unit): String {
    val compileTest = CompileTest()
    println("$compileTest -_--------------------------------------------------------------------------------------")
    compileTest.block()
    return compileTest.toString()
}

inline fun CompileTest.imports(block: Imports.() -> Unit) {
    imports.block()
}

inline fun CompileTest.fileAnnotations(block: FileAnnotations.() -> Unit) {
    fileAnnotations.block()
}

inline fun CompileTest.mainClass(block: PropertiesClass.() -> Unit) {
    add(PropertiesClass("TestClass", this).apply(block))
}

inline fun ClassContainer.compileClass(name: String, block: CompileClass.()->Unit) {
    add(CompileClass(name, compileTest).apply(block))
}

inline fun ClassContainer.propertiesClass(name: String, block: PropertiesClass.()->Unit) {
    add(PropertiesClass(name, compileTest).apply(block))
}

inline fun CompileClass.constructor(block: PrimaryConstructor.() -> Unit) = constructor.block()
inline fun PropertiesClass.propertiesAnnotation(block: PropertiesAnnotation.() -> Unit) = propertiesAnnotation.block()

interface CompileTestComponent {
    abstract val compileTest: CompileTest
}

interface AnnotatedContainer : CompileTestComponent {
    fun add(annotation: String)
}

interface ClassContainer : CompileTestComponent {
    fun add(klass: CompileClass)
}

fun <T : Annotation> AnnotatedContainer.add(annotation: KClass<T>, vararg parameter: String, import: Boolean = true) {
    val name = if (import) {
        compileTest.imports.import(annotation)
        annotation.simpleName!!
    } else {
        annotation.qualifiedName!!
    }
    add(parameter.joinToString(prefix = "$name(", postfix = ")"))
}

class Imports(override val compileTest: CompileTest) : CompileTestComponent {
    val imports: HashSet<String> = HashSet()
    fun import(type: String) {
        imports.add("import $type")
    }

    fun import(type: KClass<*>) {
        import(type.qualifiedName!!)
    }

    fun <T : Enum<T>> importEnum(enum: T) {
        import("${enum::class.qualifiedName!!}.$enum")
    }

    operator fun KClass<*>.unaryPlus() = import(this)

    override fun toString(): String {
        return buildString {
            for (import in imports) {
                append(import).append('\n')
            }
        }
    }
}

class FileAnnotations(override val compileTest: CompileTest) : AnnotatedContainer {
    val statements: MutableList<String> = ArrayList()
    override fun add(annotation: String) {
        statements.add("@file:$annotation")
    }

    override fun toString(): String {
        return buildString {
            for (statement in statements)
                append(statement).append('\n')
        }
    }
}

class PropertiesAnnotation(override val compileTest: CompileTest) : CompileTestComponent {
    companion object {
        val propertyAnnotationName = Properties::class.qualifiedName!!
        val propertiesVisibilityName = Properties.PropertiesVisibility::class.qualifiedName!!
        val generationStanceName = GenerationStance::class
    }

    var visibility = ""
    var generateBlocking = ""
    var generateAsync = ""
    var cascade: Boolean? = null

    var importAnnotation = true
        set(value) {
            field = value
            if (value) {
                compileTest.imports.import(propertyAnnotationName)
            } else {
                compileTest.imports.imports.removeIf { it.contains(propertyAnnotationName) }
            }
        }

    init {
        compileTest.imports.import(propertyAnnotationName)
    }

    fun visibility(
        visibility: Properties.PropertiesVisibility,
        import: Boolean = true,
        completeImport: Boolean = false
    ) {
        when {
            completeImport -> {
                this.visibility = visibility.toString()
                compileTest.imports.importEnum(visibility)
            }
            import -> {
                this.visibility = "PropertiesVisibility.$visibility"
                compileTest.imports.import(propertiesVisibilityName)
            }
            else -> {
                this.visibility = "${propertiesVisibilityName}.$visibility"
            }
        }
    }

    private fun generationStance(generationStance: GenerationStance, import: Boolean, completeImport: Boolean): String {
        return when {
            completeImport -> {
                compileTest.imports.importEnum(generationStance)
                generationStance.toString()
            }
            import -> {
                compileTest.imports.import(generationStanceName)
                "GenerationStance.$generationStance"
            }
            else -> {
                "$generationStanceName.$generationStance"
            }
        }
    }

    fun generateBlocking(generationStance: GenerationStance, import: Boolean = true, completeImport: Boolean = false) {
        generateBlocking = generationStance(generationStance, import, completeImport)
    }

    fun generateAsync(generationStance: GenerationStance, import: Boolean = true, completeImport: Boolean = false) {
        generateAsync = generationStance(generationStance, import, completeImport)
    }


    override fun toString(): String {
        return buildString {
            append("@Properties(")
            if (visibility.isNotBlank()) append(visibility).append(", ")
            if (generateBlocking.isNotBlank()) append(generateBlocking).append(", ")
            if (generateAsync.isNotBlank()) append(generateAsync).append(", ")
            if (cascade != null) append(cascade)
            append(')')
        }
    }
}

open class CompileClass(val name: String, override val compileTest: CompileTest) : ClassContainer, AnnotatedContainer {
    val annotations: MutableList<String> = ArrayList()
    val classes: MutableList<CompileClass> = ArrayList()
    val constructor = PrimaryConstructor(compileTest)
    var typeModifier = "class"
    val modifiers: MutableList<String> = ArrayList()

    override fun add(annotation: String) {
        annotations.add("@$annotation")
    }

    override fun add(klass: CompileClass) {
        classes.add(klass)
    }

    override fun toString(): String {
        return buildString {
            annotations.forEach {
                append(it).append('\n')
            }
            append("${modifiers.joinToString(separator = "")} $typeModifier $name $constructor { \n")
            for (klass in classes) {
                append(klass)
                append('\n')
            }
            append('}')
        }
    }
}

class PropertiesClass(name: String, compileTest: CompileTest) : CompileClass(name, compileTest) {
    val propertiesAnnotation = PropertiesAnnotation(compileTest)

    override fun toString(): String {
        return "$propertiesAnnotation\n${super.toString()}"
    }
}

class PrimaryConstructor(override val compileTest: CompileTest) : AnnotatedContainer {
    val annotations: MutableList<String> = ArrayList()
    val parameters: MutableList<String> = ArrayList()

    fun addProperty(name: String, type: KClass<*>, isVar: Boolean = false, isVal: Boolean = false) {
        compileTest.imports.import(type)
        val modifier = when {
            isVar -> "var "
            isVal -> "val"
            else -> ""
        }
        parameters.add("$modifier$name: ${type.simpleName!!}")
    }

    fun Param(name: String, type: KClass<*>) = addProperty(name, type)
    fun Var(name: String, type: KClass<*>) = addProperty(name, type, isVar = true)
    fun Val(name: String, type: KClass<*>) = addProperty(name, type, isVal = true)

    override fun add(annotation: String) {
        annotations.add("@$annotation")
    }

    override fun toString(): String {
        return "${annotations.joinToString(separator = " ")} public constructor(${parameters.joinToString()})"
    }
}

fun KotlinCompilation.Result.testClass(): KClass<*> {
    return try {
        classLoader.loadClass("TestClass").kotlin
    } catch (e: ClassNotFoundException) {
        throw AssertionError("TestClass was not compiled")
    }
}

fun KClass<*>.findFactoryImplementation(): KClass<*> {
    return nestedClasses.firstOrNull { it.simpleName == "PropertiesFactory" && it.objectInstance != null }
        ?: throw AssertionError("no factory implementation found")
}

data class LoadingMethods(val blockingLoader: KFunction<*>?, val asyncLoader: KFunction<*>?)

fun KClass<*>.loadingMethods(): LoadingMethods {
    var blockingLoad: KFunction<*>? = null
    var asyncLoad: KFunction<*>? = null
    companionObject?.declaredFunctions?.forEach {
        if (it.name == "load" && it.parameters.size == 1 && it.returnType == this) {
            when (it.parameters.first().type) {
                PropertyLoader::class -> blockingLoad = it
                AsyncPropertyLoader::class -> asyncLoad = it
            }
        }
    }
    return LoadingMethods(blockingLoad, asyncLoad)
}

