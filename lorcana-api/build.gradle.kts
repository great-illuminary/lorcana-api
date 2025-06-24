import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.serialization)
    id("jvmCompat")
}

val mainClassInManifest = "eu.codlab.lorcana.api.ApplicationKt"

group = rootProject.ext["groupId"] as String
version = rootProject.ext["version"] as String

application {
    mainClass.set(mainClassInManifest)
}

dependencies {
    api(libs.kotlinx.coroutines)
    api(libs.kotlinx.coroutines.core.jvm)
    api(libs.kotlinx.coroutines.jvm)
    api(libs.ktor.server.core)
    api(libs.ktor.server.netty)
    api(libs.ktor.server.contentnegociation)
    api(libs.ktor.server.compression)
    api(libs.ktor.server.json)
    api(libs.ktor.server.cors)
    api(libs.ktor.server.auth)
    api(libs.ktor.server.auth.jwt)
    api(libs.ktor.server.auto.head.response)
    api(libs.ktor.server.websockets)
    api(libs.ktor.server.openapi)

    api("io.bkbn:kompendium-core:4.0.1")

    api(additionals.kotlinx.serialization.json)

    api(libs.file.access)

    api(libs.sentry)

    testApi(libs.ktor.server.tests.jvm)
    testApi(kotlin("test"))

    api(libs.kotlinx.coroutines.jvm)

    api(additionals.multiplatform.platform)

    api(libs.lorcana.data)
    api(project(":lorcana-dreamborn"))
    api(project(":lorcana-rph"))
}

tasks {
    withType<KotlinCompile> {
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
