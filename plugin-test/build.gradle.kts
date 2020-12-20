plugins {
    kotlin("jvm") apply true
    id("symbol-processing") version "1.4.20-dev-experimental-20201204"
}

repositories {
    google()
    mavenCentral()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>() {
    kotlinOptions.jvmTarget = project.property("jvmVersion")!! as String
}

ksp {
    arg("io.config4k.static.loaderSource", rootProject.rootDir.toPath()
        .resolve("buildSrc")
        .resolve("build")
        .resolve("libs")
        .resolve("buildSrc.jar").toUri().toString()
    )
    arg("io.config4k.static.loader", "test.Subclass|DaClass.propertyLoader")
}


dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":json"))
    implementation(project(":java-io"))
    implementation(project(":annotation"))
    implementation(project(":common"))
    implementation(project(":ksp-plugin"))
    ksp(project(":ksp-plugin"))
}
