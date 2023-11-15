import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version libs.versions.kotlin apply false
    id("com.android.library") version libs.versions.androidGradlePlugin apply false
    id("org.jetbrains.kotlin.android") version libs.versions.kotlin apply false
}

allprojects {
    group = "com.ekezet.hurok"
    version = "0.1.0"
}

subprojects {
    tasks.withType<JavaCompile> {
        sourceCompatibility = JavaVersion.VERSION_17.toString()
        targetCompatibility = JavaVersion.VERSION_17.toString()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_17.toString()
        }
    }
}
