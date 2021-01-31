package io.config4k.plugin

import io.config4k.annotation.Properties.PropertiesVisibility
import kotlin.reflect.KVisibility
import kotlin.test.Test
import kotlin.test.assertEquals

class TestVisibilities {

    private fun description(visibility: String) = "trying to compile a property class with $visibility visibility"

    private inline fun testVisibility(description: String, expected: KVisibility, block: CompileTest.() -> Unit): Unit =
        expectCompileTestSuccess(description, block).testClass().let {
            val factory = it.findFactoryImplementation()
            assertEquals(
                expected,
                factory.visibility,
                "expected the factory implementation to have $expected visibility"
            )
            val (blockingLoad, asyncLoad) = it.loadingMethods()
            blockingLoad?.let { load ->
                assertEquals(
                    expected,
                    load.visibility,
                    "expected the blocking load implementation to have $expected visibility"
                )
            }
            asyncLoad?.let { load ->
                assertEquals(
                    expected,
                    load.visibility,
                    "expected the async load implementation to have $expected visibility"
                )
            }
        }

    @Test
    fun testWorkingPublic() =
        testVisibility(
            "${description("public")} with normal import",
            KVisibility.PUBLIC
        ) {
            mainClass {
                propertiesAnnotation {
                    visibility(PropertiesVisibility.PUBLIC)
                }
            }
        }

    @Test
    fun testWorkingPublicDirect() =
        testVisibility(
            "${description("public")} with direct import",
            KVisibility.PUBLIC
        ) {
            mainClass {
                propertiesAnnotation {
                    visibility(PropertiesVisibility.PUBLIC, completeImport = true)
                }
            }
        }

    @Test
    fun testWorkingPublicNoImport() =
        testVisibility("${description("public")} without import", KVisibility.PUBLIC) {
            mainClass {
                propertiesAnnotation {
                    visibility(PropertiesVisibility.PUBLIC, import = false)
                }
            }
        }

    @Test
    fun testWorkingInternal() =
        testVisibility("${description("internal")} with normal import", KVisibility.INTERNAL) {
            mainClass {
                propertiesAnnotation {
                    visibility(PropertiesVisibility.INTERNAL)
                }
            }
        }

    @Test
    fun testWorkingInternalDirect() =
        testVisibility("${description("internal")} with direct import", KVisibility.INTERNAL) {
            mainClass {
                propertiesAnnotation {
                    visibility(PropertiesVisibility.INTERNAL, completeImport = true)
                }
            }
        }

    @Test
    fun testWorkingInternalNoImport() =
        testVisibility("${description("internal")} without import", KVisibility.INTERNAL) {
            mainClass {
                propertiesAnnotation {
                    visibility(PropertiesVisibility.INTERNAL, import = false)
                }
            }
        }

    @Test
    fun testWorkingPrivate() =
        testVisibility("${description("private")} with normal import", KVisibility.PRIVATE) {
            mainClass {
                propertiesAnnotation {
                    visibility(PropertiesVisibility.PRIVATE)
                }
            }
        }

    @Test
    fun testWorkingPrivateDirect() =
        testVisibility("${description("private")} with direct import", KVisibility.PRIVATE) {
            mainClass {
                propertiesAnnotation {
                    visibility(PropertiesVisibility.PRIVATE, completeImport = true)
                }
            }
        }


    @Test
    fun testWorkingPrivateNoImport() =
        testVisibility("${description("private")} without import", KVisibility.PRIVATE) {
            mainClass {
                propertiesAnnotation {
                    visibility(PropertiesVisibility.PRIVATE, import = false)
                }
            }
        }

    private fun sameAsClassDescription(actualVisibility: String): String =
        "${description("same as class")} and class visibility of $actualVisibility"

    @Test
    fun testWorkingSameAsClass() =
        testVisibility("${sameAsClassDescription("public")} with normal import", KVisibility.PUBLIC) {
            mainClass {
                propertiesAnnotation {
                    visibility(PropertiesVisibility.SAME_AS_CLASS)
                }
                modifiers += "public"
            }
        }

    @Test
    fun testWorkingSameAsClassDirect() =
        testVisibility("${sameAsClassDescription("public")} with direct import", KVisibility.PUBLIC) {
            mainClass {
                propertiesAnnotation {
                    visibility(PropertiesVisibility.SAME_AS_CLASS, completeImport = true)
                }
                modifiers += "public"
            }
        }

    @Test
    fun testWorkingSameAsClassNoImport() =
        testVisibility("${sameAsClassDescription("public")} without import", KVisibility.PUBLIC) {
            mainClass {
                propertiesAnnotation {
                    visibility(PropertiesVisibility.SAME_AS_CLASS, import = false)
                }
                modifiers += "public"
            }
        }

    @Test
    fun testWorkingSameAsClassImplicitPublic() =
        testVisibility(sameAsClassDescription("no visibility(implicit public)"), KVisibility.PUBLIC) {
            mainClass {
                propertiesAnnotation {
                    visibility(PropertiesVisibility.SAME_AS_CLASS)
                }
            }
        }

    @Test
    fun testWorkingSameAsClassInternal() =
        testVisibility(sameAsClassDescription("internal"), KVisibility.INTERNAL) {
            mainClass {
                propertiesAnnotation {
                    visibility(PropertiesVisibility.SAME_AS_CLASS)
                }
                modifiers += "internal"
            }
        }


    @Test
    fun testWorkingSameAsClassPrivate() =
        testVisibility(sameAsClassDescription("private"), KVisibility.PRIVATE) {
            mainClass {
                propertiesAnnotation {
                    visibility(PropertiesVisibility.SAME_AS_CLASS)
                }
                modifiers += "private"
            }
        }


    @Test
    fun testFailingSameAsClassProtected(): Unit =
        expectCompileTestFailure(sameAsClassDescription("protected")) {
            mainClass {
                propertiesAnnotation {
                    visibility(PropertiesVisibility.SAME_AS_CLASS)
                }
                modifiers += "protected"
            }
        }

    private fun failingToHighVisibility(pVisibility: String, cVisibility: String): Unit =
        expectCompileTestFailure("${description(pVisibility)} with $cVisibility class visibility") {
            mainClass {
                propertiesAnnotation {
                    visibility(PropertiesVisibility.valueOf(pVisibility.toUpperCase()))
                }
                modifiers += cVisibility
            }
        }


    @Test
    fun testFailingInternalAndPrivate(): Unit = failingToHighVisibility("internal", "private")

    @Test
    fun testFailingPublicAndPrivate(): Unit = failingToHighVisibility("public", "private")

    @Test
    fun testFailingInternalAndProtected(): Unit = failingToHighVisibility("internal", "protected")

    @Test
    fun testFailingPublicAndProtected(): Unit = failingToHighVisibility("public", "protected")

    @Test
    fun testFailingPublicAndInternal(): Unit = failingToHighVisibility("public", "internal")


}