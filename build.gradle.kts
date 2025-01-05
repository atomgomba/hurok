import kotlinx.kover.gradle.plugin.dsl.GroupingEntityType

plugins {
    // trick: for the same plugin versions in all sub-modules
    alias(libs.plugins.androidLibrary).apply(false)
    alias(libs.plugins.composeCompiler).apply(false)
    alias(libs.plugins.composeMultiplatform).apply(false)
    alias(libs.plugins.kotlinMultiplatform).apply(false)

    // apply coverage to whole project
    alias(libs.plugins.kover)
}

allprojects {
    group = "com.ekezet.hurok"
    version = "1.3.0"
}

dependencies {
    kover(project(":base"))
    kover(project(":compose"))
    kover(project(":test"))
}

kover {
    reports {
        filters {
            excludes {
                annotatedBy(
                    "androidx.compose.runtime.Composable",
                    "com.ekezet.hurok.test.CoverageIgnore",
                )
            }
        }

        total {
            verify {
                rule {
                    groupBy = GroupingEntityType.CLASS
                    minBound(80)
                }
            }
        }
    }
}
