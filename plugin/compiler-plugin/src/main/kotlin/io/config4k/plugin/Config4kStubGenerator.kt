package io.config4k.plugin

import io.config4k.annotation.GenerationStance
import io.config4k.annotation.Properties
import io.config4k.annotation.Properties.PropertiesVisibility
import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.com.intellij.lang.ASTFactory
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.openapi.project.Project
import org.jetbrains.kotlin.com.intellij.patterns.IElementTypePattern
import org.jetbrains.kotlin.com.intellij.psi.FileViewProvider
import org.jetbrains.kotlin.com.intellij.psi.FileViewProviderFactory
import org.jetbrains.kotlin.com.intellij.psi.tree.IElementType
import org.jetbrains.kotlin.com.intellij.util.io.StringRef
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.CompilerConfigurationKey
import org.jetbrains.kotlin.extensions.CollectAdditionalSourcesExtension
import org.jetbrains.kotlin.fir.expressions.builder.buildWhenBranch
import org.jetbrains.kotlin.lexer.KtModifierKeywordToken
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.addRemoveModifier.addModifier
import org.jetbrains.kotlin.psi.psiUtil.visibilityModifierType
import org.jetbrains.kotlin.psi.stubs.KotlinFileStub
import org.jetbrains.kotlin.psi.stubs.impl.KotlinFileStubImpl
import org.jetbrains.kotlin.psi.stubs.impl.KotlinFunctionStubImpl

class Config4kStubGenerator(private val config4kContext: Config4kContext) : CollectAdditionalSourcesExtension {
    override fun collectAdditionalSourcesAndUpdateConfiguration(
        knownSources: Collection<KtFile>,
        configuration: CompilerConfiguration,
        project: Project
    ): Collection<KtFile> {
        for (file in knownSources)
            generateForAllInFile(file)
        return emptyList()
    }

    private fun generateForAllInFile(file: KtFile) {
        var isPropertiesImported = false
        var isPropertiesImportedKnown = false
        var defaultGenerateBlocking: GenerationStance? = null
        var defaultGenerateAsync: GenerationStance? = null
        file.declarations.filterIsInstance<KtNamedFunction>().forEach(this::printOut)
        for (klass in file.declarations.filterIsInstance<KtClass>()) {
            printVisible("class: ${klass.name}")
            var propertyAnnotation: KtAnnotationEntry? = null
            for (currentAnnotation in klass.annotationEntries) {
                printVisible("annotation name: ${currentAnnotation.name}")
                when (currentAnnotation.shortName?.asString()) {
                    "io.config4k.annotation.Properties" -> {
                        propertyAnnotation = currentAnnotation
                        break
                    }
                    "Properties" -> {
                        if (!isPropertiesImportedKnown) {
                            isPropertiesImported = file.importDirectives.any {
                                when (it.text) {
                                    "import io.config4k.annotation.Properties",
                                    "import io.config4k.annotation.*" -> true
                                    else -> false
                                }
                            }
                            isPropertiesImportedKnown = true
                        }
                        if (isPropertiesImported) {
                            propertyAnnotation = currentAnnotation
                            break
                        }
                    }
                }
            }
            propertyAnnotation ?: continue
            printVisible(propertyAnnotation.children.toString())
            printVisible(propertyAnnotation.valueArguments.map {
                val expr = it.getArgumentExpression()
                printVisible(expr?.text.toString())
                it.getArgumentName()?.asName?.identifier
                it.getArgumentExpression()?.let { it::class.qualifiedName } ?: ""
            }.toString())
            var generateBlocking: GenerationStance? = null
            var generateAsync: GenerationStance? = null
            var visibility: PropertiesVisibility? = null
            for (annotationEntry in propertyAnnotation.valueArguments) {
                when (annotationEntry.getArgumentName()?.asName?.identifier) {
                    "generateBlocking" ->
                        generateBlocking =
                            annotationEntry.getArgumentExpression()?.text?.parseGenerationStance()
                    "generateAsync" ->
                        generateAsync = annotationEntry.getArgumentExpression()?.text?.parseGenerationStance()
                    "visibility" ->
                        visibility = annotationEntry.getArgumentExpression()?.let { parseVisibility(it.text) }
                }
            }
            if (visibility == null || visibility == PropertiesVisibility.DEFAULT)
                visibility = config4kContext.defaultVisibility
            if (generateBlocking == null || generateBlocking == GenerationStance.DEFAULT) {
                if (defaultGenerateBlocking == null)
                    defaultGenerationStance(file, config4kContext).let {
                        defaultGenerateBlocking = it.first
                        defaultGenerateAsync = it.second
                    }
                generateBlocking = defaultGenerateBlocking
            }
            if (generateBlocking == GenerationStance.GENERATE) {
                file.add(generateFunctionStub(file, klass, visibility, false))
            }
            if (generateAsync == null || generateBlocking == GenerationStance.DEFAULT) {
                if (defaultGenerateAsync == null)
                    defaultGenerationStance(file, config4kContext).let {
                        defaultGenerateBlocking = it.first
                        defaultGenerateAsync = it.second
                    }
                generateAsync = defaultGenerateAsync
            }
            if (generateAsync == GenerationStance.GENERATE) {
                file.add(generateFunctionStub(file, klass, visibility, true))
            }
        }
    }

    private fun defaultGenerationStance(
        file: KtFile,
        context: Config4kContext
    ): Pair<GenerationStance, GenerationStance> {
        return file
            .annotationEntries
            .firstOrNull { it.shortName?.identifier == "io.config4k.annotation.DefaultGenerationStance" }
            .let {
                var generateBlocking: GenerationStance? = null
                var generateAsync: GenerationStance? = null
                if (it != null) {
                    for (param in it.valueArguments) {
                        when (param.getArgumentName()?.asName?.identifier) {
                            "generateBlocking" -> param.getArgumentExpression()?.text?.parseGenerationStance()
                            "generateAsync" -> param.getArgumentExpression()?.text?.parseGenerationStance()
                        }
                    }
                }
                if (generateBlocking == null || generateBlocking == GenerationStance.DEFAULT)
                    generateBlocking =
                        if (context.defaultGenerateBlocking)
                            GenerationStance.GENERATE
                        else
                            GenerationStance.DONT_GENERATE
                if (generateAsync == null || generateAsync == GenerationStance.DEFAULT)
                    generateAsync =
                        if (context.defaultGenerationAsync)
                            GenerationStance.GENERATE
                        else
                            GenerationStance.DONT_GENERATE
                generateBlocking to generateAsync
            }
    }


    private fun generateFunctionStub(
        file: KtFile,
        parentClass: KtClass,
        visibility: PropertiesVisibility,
        isSuspend: Boolean
    ): KtFunction {
        return KtNamedFunction(
            KotlinFunctionStubImpl(
                parent = KotlinFileStubImpl(file, "hello", false),
                nameRef = StringRef.fromString("${parentClass.name}"),
                isTopLevel = true,
                fqName = parentClass.fqName,
                isExtension = false,
                hasBlockBody = false,
                hasBody = true,
                hasTypeParameterListBeforeFunctionName = false,
                mayHaveContract = true
            )
        )
    }

    private fun tokenVisibility(
        visibility: PropertiesVisibility,
        klass: KtClass
    ): KtModifierKeywordToken {
        return when (visibility) {
            PropertiesVisibility.PUBLIC -> KtTokens.PUBLIC_KEYWORD
            PropertiesVisibility.INTERNAL -> KtTokens.INTERNAL_KEYWORD
            PropertiesVisibility.PRIVATE -> KtTokens.PRIVATE_KEYWORD
            PropertiesVisibility.SAME_AS_CLASS -> {
                val classVisibility = klass.visibilityModifierType()
                if (classVisibility == KtTokens.PROTECTED_KEYWORD)
                    throw IllegalStateException(
                        "protected visibility unsupported for properties class with visibility SAME_AS_CLASS"
                    )
                classVisibility ?: KtTokens.PUBLIC_KEYWORD
            }
            PropertiesVisibility.DEFAULT -> error("default visibility encountered")
        }
    }

    private fun printOut(function: KtNamedFunction) {
        val message = StringBuilder()
        message.append("class name: ${function::class.qualifiedName}, functionName: ${function.name}\n")
        message.append("-ast: ${function.node::class}")
        for (valueParam in function.valueParameters) {
            message.append("-value param class: ${valueParam::class} ${valueParam.name}, type: ${valueParam.typeReference?.typeElement?.text}\n")
        }
        message.append("-return ${function.typeReference?.typeElement?.text}\n")
        message.append("-body ${(function.bodyExpression as? KtCallExpression)?.text}")
        printVisible(message.toString())
    }

    private fun String.parseGenerationStance(): GenerationStance {
        return when {
            endsWith("GENERATE") -> GenerationStance.GENERATE
            endsWith("DONT_GENERATE") -> GenerationStance.DONT_GENERATE
            endsWith("DEFAULT") -> GenerationStance.DEFAULT
            else -> throw IllegalArgumentException("$this is not of type GenerationStance")
        }
    }

    private fun parseVisibility(string: String): PropertiesVisibility {
        return when {
            string.endsWith("PUBLIC") -> PropertiesVisibility.PUBLIC
            string.endsWith("INTERNAL") -> PropertiesVisibility.INTERNAL
            string.endsWith("PRIVATE") -> PropertiesVisibility.PRIVATE
            string.endsWith("SAME_AS_CLASS") -> PropertiesVisibility.SAME_AS_CLASS
            string.endsWith("DEFAULT") -> PropertiesVisibility.DEFAULT
            else -> throw java.lang.IllegalArgumentException("$string is not of type Visibility")
        }
    }
}