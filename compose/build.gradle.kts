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
                implementation(libs.compose.runtime)
                implementation(libs.common.lifecycle.viewmodelCompose)
                implementation(libs.kotlinx.coroutines)

                api(projects.base)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.compose.ui.test)
                implementation(libs.kotlin.test)
                implementation(libs.kotlinx.coroutines.test)
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
