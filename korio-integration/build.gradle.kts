plugins {
    id("c4k-build-plugin")
    `maven-publish`
}

repositories {
    maven("https://dl.bintray.com/korlibs/korlibs/")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                val korlibVersion: String by project
                implementation("com.soywiz.korlibs.korio:korio:2.0.2")
            }
        }
    }
}
