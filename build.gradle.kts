plugins {
    kotlin("multiplatform") apply false
}

allprojects {
    group = "io.config4k"
    version = project.property("propertyLoaderVersion")!!

    repositories {
        mavenCentral()
        jcenter()
        google()
    }
}