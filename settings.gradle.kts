pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        kotlin("multiplatform") version kotlinVersion apply false
    }
    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "symbol-processing" ->
                    useModule("com.google.devtools.ksp:symbol-processing:${requested.version}")
            }
        }
    }
    repositories {
        gradlePluginPortal()
        mavenCentral()
        jcenter()
        google()
    }
}
rootProject.name = "PropertyLoading"

include("common")
include("json")
include("java-io")
include("annotation")
include("ksp-plugin")
include("plugin-test")
include("property-loader-test-suite")
include("property-format")
include("korio-integration")
