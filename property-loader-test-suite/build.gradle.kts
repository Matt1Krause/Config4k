@file:Suppress("UNUSED_VARIABLE")

plugins {
    kotlin("multiplatform")
    `maven-publish`
}

publishing {
    publications {
        create<MavenPublication>("maven")
    }
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
                val xCoroutinesVersion: String by project
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$xCoroutinesVersion")
                implementation(project(":common"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val commonTest by getting
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
        val jvmTest by getting
        val jsMain by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
        val jsTest by getting
        val nativeMain by getting
        val nativeTest by getting
    }
}