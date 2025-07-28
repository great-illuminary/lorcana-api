import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(additionals.plugins.kotlin.jvm)
    alias(additionals.plugins.kotlin.serialization)
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
    api(additionals.kotlinx.coroutines)
    api(additionals.kotlinx.coroutines.core)
    api(additionals.kotlinx.coroutines.jvm)

    api(libs.apache.httpclient)
    api(libs.apache.httpclient.fluent)

    api(additionals.kotlinx.serialization.json)

    api(additionals.multiplatform.file.access)

    api(libs.sentry)

    testApi(kotlin("test"))

    api(additionals.kotlinx.coroutines.jvm)
    api(additionals.multiplatform.http.client)

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
