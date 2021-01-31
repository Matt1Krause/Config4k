plugins {
    java
    `kotlin-dsl`
}

dependencies {
    val kotlinVersion: String by rootProject
    println(embeddedKotlinVersion)
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.20")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin-model:1.4.20")
    implementation("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.5")
    implementation(gradleApi())
    implementation(gradleKotlinDsl())
    implementation(localGroovy())
}

repositories { jcenter() }