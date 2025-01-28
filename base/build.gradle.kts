import com.ekezet.hurok.buildLogic.ProjectDefaults

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kover)
    id("module.publication")
}

dependencies {
    testImplementation(platform(libs.junit5.bom))
    testImplementation(libs.junit5.jupiter)
    testRuntimeOnly(libs.junit5.platform)

    dokkaPlugin(libs.dokka.mermaid)
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
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.kotlinx.coroutines.test)
            }
        }

        val jvmMain by getting {
        }

        val jvmTest by getting {
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
    moduleName = "hurok-base"
    modulePath = "base"

    dokkaSourceSets.configureEach {
        includes.from("${rootProject.rootDir}/docs/include/index-base.md")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
