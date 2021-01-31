package io.config4k.plugin

import io.config4k.annotation.Properties
import io.config4k.plugin.codegen.CodeGeneration
import io.config4k.plugin.schema.SchemaGenerator
import io.config4k.plugin.schema.SourceScheme
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.ir.builders.TranslationPluginContext
import org.jetbrains.kotlin.ir.declarations.IrDeclaration
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.symbols.IrFunctionSymbol
import org.jetbrains.kotlin.ir.symbols.IrSymbol
import org.jetbrains.kotlin.ir.util.dump
import kotlin.reflect.jvm.internal.impl.name.FqName

class Config4kIrGenerationExtension(private val messageCollector: MessageCollector,private val c4kBuiltIns: Config4kBuiltIns) :
    IrGenerationExtension{

    companion object {
        val propertiesName = Properties::class.qualifiedName!!
        val propertiesFqName = FqName(propertiesName)
    }

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        printVisible("ir generation")
        val config4kContext = Config4kContext()
        val schemaGen = SchemaGenerator(messageCollector, config4kContext, c4kBuiltIns, pluginContext)
        moduleFragment.files.flatMap { schemaGen.from(it) }
        val schemaValues = schemaGen.schemas
        val schemas = schemaValues.map {
            it.schemaValue() ?: return
        }
        val codeGen = CodeGeneration(config4kContext, pluginContext, schemas)
        schemas.forEach {
            if (it is SourceScheme) codeGen.generate(it)
        }
    }

    override fun resolveSymbol(symbol: IrSymbol, context: TranslationPluginContext): IrDeclaration? {
        printVisible("resolve")
        if (symbol !is IrFunctionSymbol) return null
        println(symbol.owner.dump())
        return null
    }
}