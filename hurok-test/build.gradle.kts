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

                implementation(projects.hurokBase)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)

                implementation(projects.hurokBase)
            }
        }

        val jvmTest by getting { }
    }
}

dokka {
    dokkaSourceSets.configureEach {
        includes.from("${rootProject.rootDir}/docs/include/index-test.md")
    }
}
