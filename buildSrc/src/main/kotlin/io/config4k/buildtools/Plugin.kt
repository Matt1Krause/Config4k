package io.config4k.buildtools

import io.config4k.buildtools.Config4kBuildExtension.Target
import com.jfrog.bintray.gradle.BintrayExtension
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.plugins.PublishingPlugin
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

open class Plugin : Plugin<Project> {
    override fun apply(target: Project) {
        val ext: Config4kBuildExtension = Config4kBuildExtension(target.properties)
        //val ext = target.extensions.create("config4kBuild", io.config4k.build.Config4kBuildExtension::class)
        configureRepositories(target)
        configureMPP(target, ext)
        configurePublish(target, ext)
    }

    private fun configureRepositories(project: Project) {
        project.repositories.apply {
            jcenter()
            google()
            gradlePluginPortal()
        }
    }

    private fun configureMPP(project: Project, extension: Config4kBuildExtension) {
        project.plugins.apply("org.jetbrains.kotlin.multiplatform")
        project.extensions.findByType<KotlinMultiplatformExtension>()?.run {
            extension.onContains(Target.JVM) {
                jvm {
                    compilations.all {
                        kotlinOptions.jvmTarget = project.property("jvmVersion")!! as String
                    }
                    testRuns["test"].executionTask.configure {
                        useJUnit()
                    }
                }
            }
            extension.onContains(Target.JS) {
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
            }
            extension.onContains(Target.NATIVE) {
                val hostOs = System.getProperty("os.name")
                val isMingwX64 = hostOs.startsWith("Windows")
                val nativeTarget = when {
                    hostOs == "Mac OS X" -> macosX64("native")
                    hostOs == "Linux" -> linuxX64("native")
                    isMingwX64 -> mingwX64("native")
                    else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
                }
            }
            sourceSets.run {
                getByName("commonMain") {
                    dependencies {
                        if (extension.dependOnCommon)
                            implementation(project.rootProject.project(":common"))
                    }
                }
                getByName("commonTest") {
                    dependencies {
                        implementation(kotlin("test-common"))
                        implementation(kotlin("test-annotations-common"))
                    }
                }
                extension.onContains(Target.JVM) {
                    getByName("jvmTest").run {
                        dependencies {
                            implementation(kotlin("test-junit"))
                        }
                    }
                }
                extension.onContains(Target.JS) {
                    getByName("jsTest").run {
                        dependencies {
                            implementation(kotlin("test-js"))
                        }
                    }
                }
            }
        }
    }

    fun configurePublish(project: Project, extension: Config4kBuildExtension) {
        val publish = extension.publish ?: return
        val moduleName = publish.name ?: project.name
        println("dependOnCommon: ${extension.dependOnCommon}")
        println("targets: ${extension.dependOnCommon}")
        project.plugins.apply(PublishingPlugin::class.java)
        project.plugins.apply(com.jfrog.bintray.gradle.BintrayPlugin::class.java)

        project.extensions.getByType(PublishingExtension::class.java).run {
            publications.withType(MavenPublication::class.java).run {
                all {
                    pom {
                        name.set("Config4 ${moduleName.capitalize()}")
                        description.set(publish.desc)
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
        project.extensions.getByType(BintrayExtension::class).apply {
            val bintrayUser: String by project
            val bintrayApiKey: String by project
            user = bintrayUser
            key = bintrayApiKey
            val publications = extension.targets.map { it.name.toLowerCase() }.toTypedArray()
            setPublications(*publications, "kotlinMultiplatform")

            override = publish.override

            pkg.apply {
                repo = "config4k"
                name = moduleName
                setLicenses("Apache-2.0")
                vcsUrl = "https://github.com/Matt1Krause/Config4k.git"
                version.apply {
                    val config4kVersion: String by project
                    name = config4kVersion
                }
            }
        }
    }
}

class Config4kBuildExtension(properties: Map<in String, *>) {
    enum class Target { JVM, JS, NATIVE }
    class Publish(properties: Map<in String, *>) {
        var name: String? = properties["config4k.publish.name"]?.cast()
        var desc: String? = properties["config4k.publish.description"]!!.cast()
        var override: Boolean = properties["config4k.publish.override"]?.cast<String>()?.toBoolean()?:false
    }

    var targets: List<Target> = (properties["config4k.targets"]?.cast<String>()?.split(',')?.map {
        Target.valueOf(it.trim().toUpperCase())
    } ?: listOf(Target.JVM, Target.JS, Target.NATIVE)).also { println("targets: $it") }

    var dependOnCommon: Boolean = properties["config4k.dependOnCommon"]?.cast<String>()?.toBoolean() ?: true

    var publish: Publish?
    init {
        val doPublish = properties.containsKey("config4k.publish")
            .or(properties.entries.any { it.key?.tryCast<String>()?.startsWith("config4k.publish") == true })
            .and(!properties.containsKey("config4k.publish.no_publish"))
        publish = if (doPublish)
            Publish(properties)
        else
            null
    }
}

private inline fun <reified T> Any.cast(): T {
    return this as T
}

private inline fun <reified T> Any.tryCast(): T? {
    return this as? T
}

private inline fun Config4kBuildExtension.onContains(target: Target, block: () -> Unit) {
    if (targets.contains(target))
        block()
}