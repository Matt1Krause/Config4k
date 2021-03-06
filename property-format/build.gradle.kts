@file:Suppress("UNUSED_VARIABLE")

plugins {
    kotlin("multiplatform") apply true
}

repositories {
    mavenCentral()
}

kotlin {
    /* Targets configuration omitted. 
    *  To find out how to configure the targets, please follow the link:
    *  https://kotlinlang.org/docs/reference/building-mpp-with-gradle.html#setting-up-targets */

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
                    implementation(project(":common"))
                }
            }
            val commonTest by getting {
                dependencies {
                    implementation(kotlin("test-common"))
                    implementation(kotlin("test-annotations-common"))
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
}