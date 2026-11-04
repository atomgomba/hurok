plugins {
    id("hurok.config")
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
                implementation(libs.kotlinx.coroutines)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.kotlinx.coroutines.test)
            }
        }

        jvmMain {
            dependencies {
                implementation(libs.kotlinx.coroutines.swing)
            }
        }

        jvmTest {
            dependencies {
                implementation(libs.junit5.jupiter)
                runtimeOnly(libs.junit5.platform)
            }
        }
    }
}

dependencies {
    platform(libs.junit5.bom)
    dokkaPlugin(libs.dokka.mermaid)
}

dokka {
    dokkaSourceSets.configureEach {
        includes.from("${rootProject.rootDir}/docs/include/index-base.md")
    }
}
