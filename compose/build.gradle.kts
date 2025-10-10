import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    id("hurok.config")

    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeMultiplatform)
}

kotlin {
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

                api(projects.base)
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
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
    }
}

dokka {
    moduleName = "hurok-compose"
    modulePath = "compose"

    dokkaSourceSets.configureEach {
        includes.from("${rootProject.rootDir}/docs/include/index-compose.md")
    }
}
