package io.config4k.ksp

class GenerationEnv(of: Map<String, String>) {
    private val base: String = "config4k"
    val alwaysGeneratedFolder: Boolean = of["$base.generatedFolder"].toBoolean()
}