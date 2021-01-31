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
include("property-loader-test-suite")
include("property-format")
include("korio-integration")
include("plugin")
include("plugin:compiler-plugin")
include("plugin:compiler-plugin-native")
findProject(":plugin:compiler-plugin")?.name = "compiler-plugin"
findProject(":plugin:compiler-plugin-native")?.name = "compiler-plugin-native"
