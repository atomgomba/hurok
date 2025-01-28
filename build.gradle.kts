import kotlinx.kover.gradle.plugin.dsl.GroupingEntityType

plugins {
    // trick: for the same plugin versions in all sub-modules
    alias(libs.plugins.androidLibrary).apply(false)
    alias(libs.plugins.composeCompiler).apply(false)
    alias(libs.plugins.composeMultiplatform).apply(false)
    alias(libs.plugins.kotlinMultiplatform).apply(false)

    // apply these to the whole project
    alias(libs.plugins.dokka)
    alias(libs.plugins.kover)
}

allprojects {
    group = "com.ekezet.hurok"
    version = "1.5.0"
}

subprojects {
    apply(plugin = rootProject.libs.plugins.dokka.get().pluginId)
}

kover {
    reports {
        filters {
            excludes {
                annotatedBy(
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

dependencies {
    kover(projects.base)
    kover(projects.compose)
    kover(projects.test)

    dokkaPlugin(libs.dokka.mermaid)

    dokka(projects.base)
    dokka(projects.compose)
    dokka(projects.test)
}
