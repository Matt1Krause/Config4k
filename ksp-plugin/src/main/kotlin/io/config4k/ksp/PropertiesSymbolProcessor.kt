package io.config4k.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.*
import io.config4k.annotation.Properties

class PropertiesSymbolProcessor : SymbolProcessor {
    private lateinit var logger: KSPLogger
    private lateinit var codeGenerator: CodeGenerator
    private lateinit var env: GenerationEnv
    private lateinit var staticResolveResources: StaticResolveResources
    override fun finish() = Unit

    override fun init(
        options: Map<String, String>,
        kotlinVersion: KotlinVersion,
        codeGenerator: CodeGenerator,
        logger: KSPLogger
    ) {
        this.logger = logger
        this.codeGenerator = codeGenerator
        this.env = GenerationEnv(options)
        //this.staticResolveResources = generateResourcesFrom(options, logger, this::class.java.classLoader)

    }

    override fun process(resolver: Resolver) {
        resolver.getSymbolsWithAnnotation(Properties::class.qualifiedName!!)
            .mapNotNull { generateSchemaFrom(it as KSClassDeclaration, logger) }
            .forEach { CodeGen(it, codeGenerator, env) }
    }
}