plugins {
    id("hurok.config")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.kotlinx.coroutines)
                implementation(libs.kotlinx.coroutines.test)

                implementation(projects.hurokBase)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)

                implementation(projects.hurokBase)
            }
        }

        jvmTest { }
    }
}

dokka {
    dokkaSourceSets.configureEach {
        includes.from("${rootProject.rootDir}/docs/include/index-test.md")
    }
}
