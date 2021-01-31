package io.config4k.plugin

import io.config4k.ComplexLoadableFactory
import io.config4k.annotation.DefaultGenerationStance
import io.config4k.annotation.Properties
import io.config4k.async.AsyncComplexLoadableFactory
import io.config4k.async.AsyncPropertyLoader
import io.config4k.blocking.BlockingComplexLoadableFactory
import io.config4k.blocking.PropertyLoader
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import kotlin.reflect.KClass

class Config4kBuiltIns {
    val propertiesAnnotationName = Properties::class.qualifiedName!!
    val propertiesAnnotationFqn = FqName(propertiesAnnotationName)
    val propertiesGenerateBlocking = Name.identifier("blockingGeneration")
    val propertiesGenerateAsync = Name.identifier("asyncGeneration")

    val blockingPropertyLoaderFqn = PropertyLoader::class.fqName()
    val blockingPropertyLoaderClassID: ClassId = ClassId.topLevel(blockingPropertyLoaderFqn)
    val asyncPropertyLoaderFqn = AsyncPropertyLoader::class.fqName()
    val asyncPropertyLoaderClassID = ClassId.topLevel(asyncPropertyLoaderFqn)

    val defaultGenerationStanceFqName = DefaultGenerationStance::class.fqName()
    val defaultGenerationStanceGenerateBlocking = propertiesGenerateBlocking
    val defaultGenerationStanceGenerateAsync = propertiesGenerateAsync
    val factoryName = Name.identifier("PropertiesFactory")

    val complexLoadableFactoryFqName = ComplexLoadableFactory::class.fqName()
    val complexLoadableFactoryClassId = ClassId.topLevel(complexLoadableFactoryFqName)
    val blockingComplexLoadableFactoryFqName = BlockingComplexLoadableFactory::class.fqName()
    val blockingComplexLoadableFactoryClassId = ClassId.topLevel(blockingComplexLoadableFactoryFqName)
    val asyncComplexLoadableFactoryFqName = AsyncComplexLoadableFactory::class.fqName()
    val asyncComplexLoadableFactoryClassId = ClassId.topLevel(asyncComplexLoadableFactoryFqName)

    val loadName = Name.identifier("load")

    private fun KClass<*>.fqName() = FqName(qualifiedName!!)
}