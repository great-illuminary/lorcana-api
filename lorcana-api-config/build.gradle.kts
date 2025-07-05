import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(additionals.plugins.kotlin.multiplatform)
    alias(additionals.plugins.multiplatform.buildkonfig)
    id("jvmCompat")
}

repositories {
    google()
    mavenCentral()
    mavenLocal()
}

kotlin {
    jvm()
}

buildkonfig {
    packageName = "eu.codlab.lorcana.config.buildconfig"

    defaultConfigs {
        buildConfigField(
            FieldSpec.Type.STRING,
            "apiLorcanaGoogleMaps",
            rootProject.extra["apiLorcanaGoogleMaps"] as String
        )
    }
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
