plugins {
    alias(additionals.plugins.kotlin.multiplatform)
    alias(additionals.plugins.kotlin.serialization)
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)
    id("jvmCompat")
}

kotlin {
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.kotlinx.coroutines)
                api(additionals.kotlinx.serialization.json)

                api(libs.file.access)

                api(libs.http.client)

                api(project(":lorcana-dreamborn-config"))
            }
        }

        val commonTest by getting {
            dependencies {
                api(kotlin("test"))
                api(libs.kotlinx.coroutines.test)
            }
        }
    }
}
