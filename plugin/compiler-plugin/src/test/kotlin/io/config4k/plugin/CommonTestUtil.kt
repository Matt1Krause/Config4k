package io.config4k.plugin

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile

import org.junit.jupiter.api.Assertions.assertEquals

inline fun expectCompileTestSuccess(description: String, block: CompileTest.()->Unit): KotlinCompilation.Result {
    val code = compileTest(block)
    throw Exception("hagvöiaerughvlaeiutgvae")
    val compileResult = compile {
        source {code}
    }
    assertEquals(
        KotlinCompilation.ExitCode.OK,
        compileResult.exitCode,
        "expected success when $description. code: \n$code"
    )
    return compileResult
}

inline fun expectCompileTestFailure(description: String, block: CompileTest.()->Unit) {
    val code = compileTest(block)
    val compileResult = compile {
        source {code}
    }
    assertEquals(
        KotlinCompilation.ExitCode.COMPILATION_ERROR,
        compileResult.exitCode,
        "expected failure when $description. code: \n$code"
    )
}

inline fun compile(block: KotlinCompilation.()->Unit): KotlinCompilation.Result {
    val kc = KotlinCompilation()
    kc.apply {
        useIR = true
        inheritClassPath = true
        compilerPlugins = listOf(Config4kComponentRegistrar())
    }
    kc.block()
    return kc.compile()
}

inline fun KotlinCompilation.source(fileName: String = "main.kt", block: ()->String) {
    sources = listOf(SourceFile.kotlin(fileName, block()))
}