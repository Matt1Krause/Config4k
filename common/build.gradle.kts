@file:Suppress("UNUSED_VARIABLE")

plugins {
    id("c4k-build-plugin")
    //kotlin("multiplatform")
    `maven-publish`
    //id("com.jfrog.bintray")
}

/*repositories {
    gradlePluginPortal()
    jcenter()
}

bintray {
    val bintrayUser: String by rootProject
    val bintrayApiKey: String by rootProject
    user = bintrayUser
    key = bintrayApiKey
    setPublications("jvm", "js", "native")

    pkg.apply {
        repo = "config4k"
        name = "common"
        setLicenses("Apache-2.0")
        vcsUrl = "https://github.com/Matt1Krause/Config4k.git"
        version.apply {
            val config4kVersion: String by rootProject
            name = config4kVersion
        }
    }
}

publishing {
    publications {
        withType<MavenPublication> {
            pom {
                name.set("Config4k Common")
                description.set("Specifications for working with properties in kotlin")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("Matt1Krause")
                        name.set("Matti Krause")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/Matt1Krause/Config4k.git")
                }
            }
        }
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
        val commonMain by getting
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
}*/