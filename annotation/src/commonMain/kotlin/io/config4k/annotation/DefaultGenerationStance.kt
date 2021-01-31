package io.config4k.annotation

@Target(AnnotationTarget.FILE)
@Retention(AnnotationRetention.SOURCE)
annotation class DefaultGenerationStance(
    val blockingGeneration: GenerationStance = GenerationStance.DEFAULT,
    val asyncGeneration: GenerationStance = GenerationStance.DEFAULT
)
