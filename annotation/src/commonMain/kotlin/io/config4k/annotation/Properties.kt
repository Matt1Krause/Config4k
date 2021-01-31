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
annotation class Properties(
    val visibility: PropertiesVisibility = PropertiesVisibility.DEFAULT,
    val blockingGeneration: GenerationStance = GenerationStance.DEFAULT,
    val asyncGeneration: GenerationStance = GenerationStance.DEFAULT,
    val cascade: Boolean = false
) {
    enum class PropertiesVisibility {
        PUBLIC, INTERNAL, PRIVATE, SAME_AS_CLASS, DEFAULT
    }
    companion object {
        const val FORCE_INTERNAL = false
        const val CASCADE = false
    }
}
