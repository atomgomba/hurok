import com.ekezet.hurok.buildLogic.ProjectDefaults

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kover)
    id("module.publication")
}

kotlin {
    applyDefaultHierarchyTemplate()

    jvm()

    androidTarget {
        publishLibraryVariants("release", "debug")
    }

    compilerOptions {
        jvmToolchain(ProjectDefaults.javaVersion.majorVersion.toInt())
    }

    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.android)
            }
        }

        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines)
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.kotlin.test)

                implementation(project(":base"))
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(project(":base"))
            }
        }
    }
}

java {
    targetCompatibility = ProjectDefaults.javaVersion
    sourceCompatibility = ProjectDefaults.javaVersion
}

android {
    namespace = "com.ekezet.hurok"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = ProjectDefaults.javaVersion
        targetCompatibility = ProjectDefaults.javaVersion
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
