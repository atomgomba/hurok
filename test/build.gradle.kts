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
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.kotlin.test)

                implementation(projects.base)
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(projects.base)
            }
        }
    }
}

dokka {
    moduleName = "hurok-test"
    modulePath = "test"

    dokkaSourceSets.configureEach {
        includes.from("${rootProject.rootDir}/docs/include/index-test.md")
    }
}
