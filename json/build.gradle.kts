@file:Suppress("UNUSED_VARIABLE")

plugins {
    kotlin("multiplatform")
    `maven-publish`
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = project.property("jvmVersion")!! as String
        }
        testRuns["test"].executionTask.configure {
            useJUnit()
        }
    }
    js(LEGACY) {
        browser {
            testTask {
                useKarma {
                    useChromeHeadless()
                    webpackConfig.cssSupport.enabled = true
                }
            }
        }
    }
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }


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