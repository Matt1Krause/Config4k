plugins {
    id("c4k-build-plugin")
    `maven-publish`
}
kotlin {
    sourceSets {
        val jvmMain by getting {
            dependencies {
                val kspVersion: String by project
                implementation(kotlin("stdlib"))
                implementation(project(":annotation"))
                implementation("com.google.devtools.ksp:symbol-processing-api:$kspVersion")
            }
        }
    }
}
