plugins {
    id("hurok.config")

    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeMultiplatform)
}

kotlin {
    sourceSets {
        androidMain {
            dependencies {
                implementation(libs.kotlinx.coroutines.android)
            }
        }

        commonMain {
            dependencies {
                implementation(libs.compose.runtime)
                implementation(libs.compose.ui)
                implementation(libs.common.lifecycle.viewmodelCompose)
                implementation(libs.kotlinx.coroutines)

                api(projects.hurokBase)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.compose.ui.test)
                implementation(libs.kotlin.test)
                implementation(libs.kotlinx.coroutines.test)
            }
        }

        jvmMain {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
    }
}

dokka {
    dokkaSourceSets.configureEach {
        includes.from("${rootProject.rootDir}/docs/include/index-compose.md")
    }
}
