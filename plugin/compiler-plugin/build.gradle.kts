import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("org.jetbrains.kotlin.plugin.noarg") version "1.4.21"
    //id("kotlin-publish")
}

noArg {
    annotation("io.config4k.plugin.TestAnnotation")
    invokeInitializers = true
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    jcenter()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.run { freeCompilerArgs = freeCompilerArgs + "-Xjvm-default=enable" }
}

dependencies {
    compileOnly("org.jetbrains.kotlin:kotlin-compiler-embeddable")
    implementation(project(":common"))
    implementation(project(":annotation"))
    implementation(kotlin("reflect"))

    kapt("com.google.auto.service:auto-service:1.0-rc7")
    compileOnly("com.google.auto.service:auto-service-annotations:1.0-rc7")

    //testImplementation(project(":debuglog-annotation"))

    testImplementation(kotlin("test-common"))
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.platform:junit-platform-launcher:1.7.0")
    testImplementation("org.jetbrains.kotlin:kotlin-compiler-embeddable")
    testImplementation("com.github.tschuchortdev:kotlin-compile-testing:1.2.6")
    testImplementation("org.bitbucket.mstrobel:procyon-compilertools:0.5.36")
    testImplementation(project(":json"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        showStandardStreams = true
        //events("passed", "skipped", "failed")
    }
}

tasks.compileTestKotlin {
    kotlinOptions {
        useIR = true
    }
}
