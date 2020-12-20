package io.config4k.annotation


/**
 * Declares the annotated class to be a property holder. it is consumed by the compiler plugin.
 * Per default the methods $ClassName(PropertyLoader) and $ClassName(AsyncPropertyLoader) will be generated
 * The visibility is the visibility of the class, unless it is forced internal by [forceInternal]. The used
 * constructor is either a constructor with [PropertiesConstructor] or the primary constructor.
 *
 * @see PropertiesConstructor
 * @see PropertyName
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
@MustBeDocumented
annotation class Properties(val forceInternal: Boolean = false)
