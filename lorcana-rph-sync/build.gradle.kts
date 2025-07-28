plugins {
    alias(additionals.plugins.kotlin.multiplatform)
    alias(additionals.plugins.kotlin.serialization)
    alias(libs.plugins.room)
    alias(additionals.plugins.ksp)
    id("jvmCompat")
}

kotlin {
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.ravensburger.play.hub)
                api(additionals.multiplatform.file.access)
                api(project(":lorcana-rph-database"))
            }
        }

        val commonTest by getting {
            dependencies {
                api(kotlin("test"))
                api(additionals.kotlinx.coroutines.test)
            }
        }
    }
}

dependencies {
    add("kspJvm", libs.androidx.room.compiler)
}

room {
    schemaDirectory("$projectDir/schemas")
}
