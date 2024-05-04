import com.ekezet.hurok.buildLogic.ProjectDefaults

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    id("module.publication")
}

kotlin {
    applyDefaultHierarchyTemplate()

    jvm {
        compilations.all {
            kotlinOptions {
                jvmTarget = ProjectDefaults.javaVersion.toString()
            }
        }
    }

    androidTarget {
        publishLibraryVariants("release", "debug")
        compilations.all {
            kotlinOptions {
                jvmTarget = ProjectDefaults.javaVersion.toString()
            }
        }
    }

    linuxX64()

    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.android)
            }
        }

        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines)
                implementation(compose.runtime)

                implementation(project(":base"))
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        val jvmMain by getting {
        }
    }
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

    dependencies {
        implementation(libs.androidx.lifecycle.viewmodelCompose)
    }
}
