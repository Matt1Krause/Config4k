plugins {
    kotlin("jvm") apply true
}

repositories {
    mavenCentral()
    google()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>() {
    kotlinOptions.jvmTarget = project.property("jvmVersion")!! as String
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":common"))
    implementation(project(":annotation"))
    implementation("com.google.devtools.ksp:symbol-processing-api:${project.property("kspVersion")}")
}
