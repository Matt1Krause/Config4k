package io.config4k.plugin

import io.config4k.plugin.schema.SchemaGenerator
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.ir.declarations.IrDeclaration

interface LoggingComponent {
    val messageCollector: MessageCollector
    val logInfo: Boolean
    val logDebug: Boolean
}

fun LoggingComponent.reportError(message: String, irDeclaration: IrDeclaration? = null) {
    messageCollector.reportError(message, irDeclaration)
}

fun LoggingComponent.reportWarning(message: String, irDeclaration: IrDeclaration? = null) {
    messageCollector.reportWarning(message, irDeclaration)
}

inline fun LoggingComponent.reportInfo0(block: ()->String) {
    if (logInfo)
        messageCollector.reportInfo(block())
}

inline fun LoggingComponent.reportInfo1(block: () -> Pair<String, IrDeclaration?>) {
    if (logInfo) {
        val (message, decl) = block()
        messageCollector.reportInfo(message, decl)
    }
}

inline fun LoggingComponent.reportDebug0(block: ()->String) {
    if (logDebug)
        messageCollector.reportDebug(block())
}

inline fun LoggingComponent.reportDebug1(block: () -> Pair<String, IrDeclaration?>) {
    if (logDebug) {
        val (message, decl) = block()
        messageCollector.reportDebug(message, decl)
    }
}

