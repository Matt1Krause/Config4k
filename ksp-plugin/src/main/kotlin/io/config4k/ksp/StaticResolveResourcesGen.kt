package io.config4k.ksp

import com.google.devtools.ksp.processing.KSPLogger
import io.config4k.async
import io.config4k.async.AsyncPropertyLoader
import io.config4k.blocking.PropertyLoader
import java.lang.reflect.Modifier
import java.net.URI
import java.net.URLClassLoader

private const val configPath = "io.config4k.static"
private const val staticSources = "$configPath.loaderSource"
private const val loaderClass = "$configPath.loader"

fun generateResourcesFrom(options: Map<String, String>, logger: KSPLogger, currentClassLoader: ClassLoader): StaticResolveResources {
    val staticResolveResources = StaticResolveResources()
    val classLoader = getClassLoaderFor(options, currentClassLoader)
    loadLoaderClassInto(staticResolveResources, options, classLoader, logger)
    return staticResolveResources
}

private fun getClassLoaderFor(options: Map<String, String>, parentClassLoader: ClassLoader): ClassLoader {
    return options.entries
        .filter { it.key.contains(staticSources) }
        .map { URI.create(it.value).toURL() }
        .toTypedArray()
        .let { URLClassLoader(it, parentClassLoader) }
}

private fun loadLoaderClassInto(
    staticResolveResources: StaticResolveResources,
    options: Map<String, String>,
    classLoader: ClassLoader,
    logger: KSPLogger,
) {
    val loadedClasses = HashMap<String, Class<*>>()
    options.entries.filter { it.key.contains(loaderClass) }.mapNotNull {
        val (PropertiesQN, propertyLoaderPath) =
            it.value.split('|').also {values ->
                if (values.size != 2) {
                    logger.error("expecting a value in form of <StaticResolvedProperty>|<fullPathToLoaderCraetingMethod>")
                    return@mapNotNull null
                }
            }
        val methodSepIndex = propertyLoaderPath.indexOfLast {c -> c == '.' }
        val className = propertyLoaderPath.substring(0, methodSepIndex)
        val alreadyLoadedClass = loadedClasses[className]
        val klass: Class<*>
        if (alreadyLoadedClass != null) {
            klass = alreadyLoadedClass
        } else {
            klass = try {
                classLoader.loadClass(className)
            } catch (e: ClassNotFoundException) {
                logger.error("class $className not found")
                return@mapNotNull null
            }
            loadedClasses[className] = klass
        }
        val methodName = propertyLoaderPath.substring(methodSepIndex + 1, propertyLoaderPath.length)
        val method = try {
            klass.getMethod(methodName)
        } catch (e: NoSuchMethodException) {
            logger.error("static no-arg method $methodName not found on $className")
            return@mapNotNull null
        }
        method.trySetAccessible()
        if (!Modifier.isStatic(method.modifiers)) {
            logger.error("the method $methodName on the class $className is not static")
        }
        val loader = when (method.returnType) {
            AsyncPropertyLoader::class.java -> method.invoke(null) as AsyncPropertyLoader
            PropertyLoader::class.java -> (method.invoke(null) as PropertyLoader).async()
            else -> {
                logger.error(
                    "The method $methodName on the class $className does not return  AsyncPropertyLoader or PropertyLoader"
                )
                return@mapNotNull null
            }
        }
        PropertiesQN to loader
    }.let {
        staticResolveResources.loaders.putAll(it)
    }
}