package io.config4k.plugin.codegen

import io.config4k.ComplexLoadableFactory
import io.config4k.LoadableFactory
import io.config4k.annotation.GenerationStance
import io.config4k.annotation.Properties
import io.config4k.async.AsyncComplexLoadableFactory
import io.config4k.async.AsyncLoadableFactory
import io.config4k.blocking.BlockingComplexLoadableFactory
import io.config4k.blocking.BlockingLoadableFactory
import io.config4k.plugin.Config4kBuiltIns
import io.config4k.plugin.Config4kContext
import io.config4k.plugin.fqName
import io.config4k.plugin.printVisible
import org.jetbrains.kotlin.backend.jvm.codegen.extractDefaultLambdaOffsetAndDescriptor
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.cli.common.messages.MessageUtil
import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.descriptors.*
import org.jetbrains.kotlin.descriptors.annotations.AnnotationDescriptor
import org.jetbrains.kotlin.js.resolve.diagnostics.findPsi
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.name.SpecialNames
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.calls.inference.model.typeForTypeVariable
import org.jetbrains.kotlin.resolve.descriptorUtil.classValueType
import org.jetbrains.kotlin.resolve.descriptorUtil.module
import org.jetbrains.kotlin.resolve.extensions.SyntheticResolveExtension
import org.jetbrains.kotlin.resolve.lazy.LazyClassContext
import org.jetbrains.kotlin.resolve.lazy.declarations.ClassMemberDeclarationProvider
import org.jetbrains.kotlin.types.*
import org.jetbrains.kotlin.utils.addToStdlib.cast
import kotlin.reflect.jvm.internal.impl.types.checker.KotlinTypeRefiner
import kotlin.reflect.typeOf

class FactoryDeclareExtension(
    private val context: Config4kContext,
    private val messageCollector: MessageCollector,
    private val c4kBuiltIns: Config4kBuiltIns
) : SyntheticResolveExtension {

    companion object {
        private val factoryObjectName = Name.identifier("PropertiesFactory")
    }

    private var asyncLoadableFactory: KotlinType? = null
    private var blockingLoadableFactory: KotlinType? = null
    private var loadableFactory: KotlinType? = null

    override fun getSyntheticNestedClassNames(thisDescriptor: ClassDescriptor): List<Name> =
        if (thisDescriptor.annotations.hasAnnotation(c4kBuiltIns.propertiesAnnotationFqn))
            listOf(factoryObjectName)
        else
            emptyList()

    override fun generateSyntheticClasses(
        thisDescriptor: ClassDescriptor,
        name: Name,
        ctx: LazyClassContext,
        declarationProvider: ClassMemberDeclarationProvider,
        result: MutableSet<ClassDescriptor>
    ) {
        super.generateSyntheticClasses(thisDescriptor, name, ctx, declarationProvider, result)
    }

    override fun generateSyntheticMethods(
        thisDescriptor: ClassDescriptor,
        name: Name,
        bindingContext: BindingContext,
        fromSupertypes: List<SimpleFunctionDescriptor>,
        result: MutableCollection<SimpleFunctionDescriptor>
    ) {
        super.generateSyntheticMethods(thisDescriptor, name, bindingContext, fromSupertypes, result)
    }

    override fun getSyntheticFunctionNames(thisDescriptor: ClassDescriptor): List<Name> {
        return super.getSyntheticFunctionNames(thisDescriptor)
    }

    override fun addSyntheticSupertypes(thisDescriptor: ClassDescriptor, supertypes: MutableList<KotlinType>) {
        if (!thisDescriptor.isCompanionObject) return
        val propertiesAnnotation = thisDescriptor
            .containingDeclaration
            .annotations
            .findAnnotation(c4kBuiltIns.propertiesAnnotationFqn)
            ?: return

        var file: PackageFragmentDescriptor? = null
        var fileDefaultGenerationStance: AnnotationDescriptor? = null
        var fileGenerationStanceComputed = false

        val blockingStance = propertiesAnnotation
            .allValueArguments[c4kBuiltIns.propertiesGenerateBlocking]
            ?.value
            ?.cast()
            ?: GenerationStance.DEFAULT

        val asyncStance = propertiesAnnotation
            .allValueArguments[c4kBuiltIns.propertiesGenerateAsync]
            ?.value
            ?.cast()
            ?: GenerationStance.DEFAULT

        val generateBlocking: Boolean = blockingStance.toBoolean {
            if (file == null) file = thisDescriptor.findParentFile()
            fileDefaultGenerationStance = file?.findDefaultGenerationStance()
            fileGenerationStanceComputed = true

            defaultGenerationStanceFile(
                fileDefaultGenerationStance,
                c4kBuiltIns.defaultGenerationStanceGenerateBlocking,
                context.defaultGenerateBlocking
            )
        }
        val generateAsync: Boolean = asyncStance.toBoolean {
            if (!fileGenerationStanceComputed) {
                if (file == null) file = thisDescriptor.findParentFile()
                fileDefaultGenerationStance = file?.findDefaultGenerationStance()
                fileGenerationStanceComputed = true
            }
            defaultGenerationStanceFile(
                fileDefaultGenerationStance,
                c4kBuiltIns.defaultGenerationStanceGenerateAsync,
                context.defaultGenerationAsync
            )
        }
        supertypes.add(
            superTypes(
                thisDescriptor.module,
                thisDescriptor.containingDeclaration as ClassDescriptor,
                generateBlocking,
                generateAsync
            ) {
                thisDescriptor.findPsi()
            } ?: return
        )
    }

    private inline fun GenerationStance.toBoolean(default: () -> Boolean): Boolean = when (this) {
        GenerationStance.GENERATE -> true
        GenerationStance.DONT_GENERATE -> false
        GenerationStance.DEFAULT -> default()
    }

    private fun defaultGenerationStanceFile(
        fileDefault: AnnotationDescriptor?,
        parameterName: Name,
        default: Boolean
    ): Boolean =
        fileDefault
            ?.allValueArguments
            ?.get(parameterName)
            ?.value
            ?.cast<GenerationStance>()
            ?.toBoolean {
                default
            } ?: default

    private inline fun superTypes(
        findIn: ModuleDescriptor,
        ofType: ClassDescriptor,
        generateBlocking: Boolean,
        generateAsync: Boolean,
        debugLocation: () -> PsiElement?
    ): KotlinType? = when {
        generateBlocking && generateAsync ->
            lazySuperType(loadableFactory, findIn, ofType, { complexLoadableFactoryClassId }) { loadableFactory = it }
        generateBlocking ->
            lazySuperType(blockingLoadableFactory, findIn, ofType, { blockingComplexLoadableFactoryClassId }) {
                blockingLoadableFactory = it
            }
        generateAsync ->
            lazySuperType(asyncLoadableFactory, findIn, ofType, { asyncComplexLoadableFactoryClassId }) {
                asyncLoadableFactory = it
            }
        else -> {
            messageCollector.report(
                CompilerMessageSeverity.ERROR,
                "it was specified that this class cannot be loaded, so it cannot be a @Properties class",
                MessageUtil.psiElementToMessageLocation(debugLocation())
            )
            null
        }
    }

    private inline fun lazySuperType(
        value: KotlinType?,
        lookup: ModuleDescriptor,
        genericType: ClassDescriptor,
        classId: Config4kBuiltIns.() -> ClassId,
        set: (KotlinType) -> Unit
    ): KotlinType {
        return if (value == null) {
            val newValue = lookup.findClassAcrossModuleDependencies(c4kBuiltIns.classId())!!.defaultType
            set(newValue)
            newValue
        } else {
            value
        }.replace(listOf(TypeProjectionImpl(genericType.defaultType)))

    }

    private fun DeclarationDescriptor.findParentFile(): PackageFragmentDescriptor? {
        val parent = containingDeclaration
        return if (parent == null || parent is PackageFragmentDescriptor)
            parent as PackageFragmentDescriptor?
        else
            parent.findParentFile()
    }

    private fun PackageFragmentDescriptor.findDefaultGenerationStance(): AnnotationDescriptor? =
        annotations.findAnnotation(
            c4kBuiltIns.defaultGenerationStanceFqName
        )

    override fun getSyntheticCompanionObjectNameIfNeeded(thisDescriptor: ClassDescriptor): Name? {
        return if (thisDescriptor.annotations.hasAnnotation(c4kBuiltIns.propertiesAnnotationFqn))
            SpecialNames.DEFAULT_NAME_FOR_COMPANION_OBJECT
        else
            null
    }
}