package io.config4k.plugin

import kotlin.test.Test

class TestApplicability {
    private fun description(type: String) = "compile a $type with properties annotation"

    private fun testApplicability(type: String): Unit = expectCompileTestFailure(description(type)) {
        mainClass {
            typeModifier = type
        }
    }

    @Test
    fun testInterface(): Unit = testApplicability("interface")

    @Test
    fun testEnum(): Unit = testApplicability("enum class")

    @Test
    fun testAbstract(): Unit = testApplicability("abstract class")

    @Test
    fun testSealed(): Unit = testApplicability("sealed class")

    @Test
    fun testInline(): Unit = expectCompileTestFailure(description("inline class")) {
        mainClass {
            constructor {
                addProperty("value", Int::class, isVal = true)
            }
            typeModifier = "inline class"
        }
    }

    @Test
    fun testInner(): Unit = expectCompileTestFailure(description("inner class")) {
        compileClass("Outer") {
            propertiesClass("Inner") {
                modifiers += "inner"
            }
        }
    }

    fun testObject(): Unit = expectCompileTestFailure(description("object")) {
        mainClass {
            typeModifier = "object"
        }
    }
}