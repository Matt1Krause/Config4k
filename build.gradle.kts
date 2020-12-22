plugins {
    kotlin("multiplatform") apply false
}

allprojects {
    group = "io.config4k"
    version = project.property("config4kVersion")!!

    repositories {
        mavenCentral()
        jcenter()
        google()
    }
}