import com.android.build.api.dsl.LibraryExtension
import com.ekezet.hurok.buildLogic.gradleExtensions.configure
import com.ekezet.hurok.buildLogic.libs
import com.vanniktech.maven.publish.MavenPublishBaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

@Suppress("unused")
class ProjectConfigPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("androidLibrary").get().get().pluginId)
                apply(libs.findPlugin("kotlinMultiplatform").get().get().pluginId)
                apply(libs.findPlugin("kover").get().get().pluginId)
                apply(libs.findPlugin("mavenPublish").get().get().pluginId)
            }

            with(extensions) {
                getByType(JavaPluginExtension::class.java).configure()
                getByType(KotlinMultiplatformExtension::class.java).configure()
                getByType(LibraryExtension::class.java).configure(target)
                getByType(MavenPublishBaseExtension::class.java).configure(target)
            }

            configureTasks()
        }
    }

    private fun Project.configureTasks() {
        tasks.withType<Test> {
            useJUnitPlatform()
        }
    }
}

