import java.util.Properties

plugins {
    java
    //kotlin("1.4.20")
}

dependencies {
    val properties = Properties()
    properties.load(project.rootDir.parentFile.resolve("gradle.properties").inputStream())
    val propertyLoaderVersion: String by properties
    implementation("io.config4k:common:$propertyLoaderVersion")
    implementation("io.config4k:json:$propertyLoaderVersion")
    implementation("io.config4k:java-io:$propertyLoaderVersion")
}

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()

}