plugins {
    id("c4k-build-plugin")
    kotlin("kapt")
}

kotlin {
    sourceSets {
        commonMain.configure {
            dependencies {
                compileOnly("org.jetbrains.kotlin:kotlin-compiler")
                compileOnly("com.google.auto.service:auto-service-annotations:1.0-rc7")
                compileOnly("com.google.auto.service:auto-service:1.0-rc7")
            }
        }
    }
}

kapt {
    annotationProcessor("com.google.auto.service.processor.AutoServiceProcessor")
}

tasks.named("compileKotlinJvm") {
    dependsOn("syncCompilerPluginSources")
}
tasks.register<Sync>("syncCompilerPluginSources") {
    from(project(":plugin:compiler-plugin").sourceSets.findByName("jvmMain")!!.allSource)
    into("src/jvmMain/kotlin")
    filter {
        when (it) {
            "import org.jetbrains.kotlin.com.intellij.mock.MockProject" -> "import com.intellij.mock.MockProject"
            else -> it
        }
    }
}