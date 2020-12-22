@file:Suppress("UNUSED_VARIABLE")

plugins {
    id("c4k-build-plugin")
    `maven-publish`
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":common")) {
                    isTransitive = true
                }
                val xSerializationJsonVersion: String by project
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$xSerializationJsonVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(project(":property-loader-test-suite"))

                val xCoroutinesVersion: String by project
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$xCoroutinesVersion")
            }
        }
        val jvmMain by getting
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
        val jsMain by getting
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
        val nativeMain by getting
        val nativeTest by getting
    }
}