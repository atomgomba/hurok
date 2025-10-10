plugins {
    id("hurok.config")
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
            dependencies {
                implementation(libs.kotlinx.coroutines.swing)
            }
        }
    }
}

dependencies {
    testImplementation(platform(libs.junit5.bom))
    testImplementation(libs.junit5.jupiter)
    testRuntimeOnly(libs.junit5.platform)

    dokkaPlugin(libs.dokka.mermaid)
}

dokka {
    moduleName = "hurok-base"
    modulePath = "base"

    dokkaSourceSets.configureEach {
        includes.from("${rootProject.rootDir}/docs/include/index-base.md")
    }
}
