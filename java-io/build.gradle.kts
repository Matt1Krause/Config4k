plugins {
    kotlin("jvm") apply true
    `maven-publish`
}

publishing {
    publications {
        create<MavenPublication>("kotlinJvm") {
            from(components["kotlin"])
        }
    }
}

repositories {
    mavenCentral()
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>() {
    kotlinOptions.jvmTarget = project.property("jvmVersion")!! as String
}

dependencies {
    implementation(project(":common")) {
        isTransitive = true
    }
}