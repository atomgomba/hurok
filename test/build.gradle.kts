plugins {
    id("hurok.config")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.kotlinx.coroutines)
                implementation(libs.kotlinx.coroutines.test)

                implementation(projects.base)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)

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
