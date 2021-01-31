package io.config4k.plugin

import kotlin.test.Test

class ShortExample {
    @Test
    fun defaultExample(): Unit = expectCompileTestSuccess("short testr") {
        mainClass {
            constructor {
                addProperty("value", Byte::class)
            }
        }
    }.let { result ->

    }
}