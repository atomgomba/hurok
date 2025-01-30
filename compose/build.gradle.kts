import com.ekezet.hurok.buildLogic.ProjectDefaults
import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kover)

    id("hurok.publishing")
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
                implementation(compose.runtime)
                implementation(libs.common.lifecycle.viewmodelCompose)
                implementation(libs.kotlinx.coroutines)

                implementation(projects.base)
            }
        }

        val commonTest by getting {
            dependencies {
                @OptIn(ExperimentalComposeLibrary::class)
                implementation(compose.uiTest)
                implementation(libs.kotlin.test)
            }
        }

        val jvmMain by getting {
        }

        val jvmTest by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
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

dokka {
    moduleName = "hurok-compose"
    modulePath = "compose"

    dokkaSourceSets.configureEach {
        includes.from("${rootProject.rootDir}/docs/include/index-compose.md")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
