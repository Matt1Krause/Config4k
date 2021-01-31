package io.config4k.plugin

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.jvm.codegen.psiElement
import org.jetbrains.kotlin.cli.common.messages.*
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.IrConst
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.types.IrSimpleType
import org.jetbrains.kotlin.ir.util.file
import org.jetbrains.kotlin.ir.util.getAnnotation
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.util.irCall
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.psiUtil.endOffset
import org.jetbrains.kotlin.psi.psiUtil.startOffset
import org.jetbrains.kotlin.resolve.source.getPsi
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

fun IrAnnotationContainer.getAnnotation(name: String): IrConstructorCall? = annotations.firstOrNull {
    (it.symbol.owner.parent as IrClass).name.identifier == name
}

@Suppress("NOTHING_TO_INLINE")
inline fun IrAnnotationContainer.getAnnotation(klass: KClass<*>): IrConstructorCall? =
    klass.fqName()?.let(this::getAnnotation)
inline fun <reified T: Annotation> IrAnnotationContainer.getAnnotation(): IrConstructorCall? = getAnnotation(T::class)

fun IrDeclarationWithName.stringName(): String = name.identifier.toString()

fun KClass<*>.fqName(): FqName? = qualifiedName?.let { FqName(it) }

inline fun <reified T: Annotation> IrAnnotationContainer.instantiateAnnotation(): T? = instantiateAnnotation(T::class)
fun <T: Annotation> IrAnnotationContainer.instantiateAnnotation(annotationClass: KClass<T>): T? {
    val annotation = getAnnotation(annotationClass) ?: kotlin.run {
        printVisible("no annotation found")
        return null
    }
    val constructor = annotationClass.primaryConstructor!!
    val parameters = hashMapOf<KParameter, Any?>()
    constructor.isAccessible = true
    for (parameter in constructor.parameters) {
        val value = annotation.getValueArgument(parameter.index) ?: continue
        val irClassifier = value.type.let { it as? IrClassSymbol }
        val classifier = parameter.type.classifier?.let { (it as? IrSimpleType) as? KClass<*> }

        if (irClassifier == null || classifier == null || irClassifier.owner.stringName() == classifier.qualifiedName) {
            throw InvalidKotlinSourceException(
                "invalid type ${irClassifier?.owner?.stringName().toString()} " +
                        "for parameter type ${parameter.type} for class ${annotationClass.qualifiedName}",
                false,
            )
        }
        parameters[parameter] = (value as IrConst<Any?>).value
    }
    return constructor.callBy(parameters)
}

class InvalidKotlinSourceException(message: String, logged: Boolean, occurredAt: CompilerMessageSourceLocation? = null):
    Exception(message)

fun printVisible(message: String) {
    println(message + "-".repeat((200 - message.length).let { if (it < 0) 0 else it }))
}

inline fun <T, U> Map<T, U>.whenPresent(key: T, block: (U)->Unit) {
    if (key in this)
        block(get(key)!!)
}

fun IrClass.declarationLocation(): CompilerMessageSourceLocation? {
    return MessageUtil.psiElementToMessageLocation(psiElement)
}

fun IrDeclaration.compilerMessageLocation(): CompilerMessageSourceLocation? {
    return MessageUtil.psiElementToMessageLocation(psiElement)
}

inline fun <T> T?.nullOnFalseOrElse(block: (T)->Boolean): Boolean {
    return if (this != null)
        block(this)
    else
        false
}

fun MessageCollector.reportError(message: String, irDeclaration: IrDeclaration? = null) {
    report(CompilerMessageSeverity.ERROR, message, irDeclaration?.compilerMessageLocation())
}

fun MessageCollector.reportWarning(message: String, irDeclaration: IrDeclaration? = null) {
    report(CompilerMessageSeverity.WARNING, message, irDeclaration?.compilerMessageLocation())
}

fun MessageCollector.reportInfo(message: String, irDeclaration: IrDeclaration? = null) {
    report(CompilerMessageSeverity.INFO, message, irDeclaration?.compilerMessageLocation())
}

fun MessageCollector.reportDebug(message: String, irDeclaration: IrDeclaration? = null) {
    report(CompilerMessageSeverity.LOGGING, message, irDeclaration?.compilerMessageLocation())
}

inline fun <reified T> IrPluginContext.referenceClass(): IrClassSymbol {
    return referenceClass(T::class.fqName()!!)!!
}

fun IrPluginContext.referenceKotlinClass(string: String): IrClassSymbol {
    return referenceClass(FqName("kotlin.$string"))!!
}