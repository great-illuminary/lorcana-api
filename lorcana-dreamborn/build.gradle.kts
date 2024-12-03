import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.sqldelight)
    id("jvmCompat")
}

sqldelight {
    databases {
        create("LocalDatabase") {
            packageName.set("eu.codlab.lorcana.dreamborn.local")
        }
    }
}

repositories {
    google()
    mavenCentral()
    mavenLocal()
}

dependencies {
    api(libs.kotlinx.coroutines)
    api(libs.kotlinx.coroutines.core.jvm)
    api(libs.kotlinx.coroutines.jvm)
    api(additionals.kotlinx.serialization.json)

    api(libs.file.access)

    api(libs.sentry)

    testApi(kotlin("test"))

    api(libs.kotlinx.coroutines.jvm)
    api(libs.http.client)

    api(libs.sqldelight.runtime)
    api(libs.sqldelight.driver.sqlite)

    api(libs.lorcana.data)

    api(project(":lorcana-dreamborn-config"))
}

tasks {
    withType<KotlinCompile> {
        dependsOn("generateSqlDelightInterface")

        kotlinOptions {
            jvmTarget = libs.versions.java.get()
            javaParameters = true
        }
    }

    withType<JavaCompile> {
        sourceCompatibility = libs.versions.java.get()
        targetCompatibility = libs.versions.java.get()
    }
}
