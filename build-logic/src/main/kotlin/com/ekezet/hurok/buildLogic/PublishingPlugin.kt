package com.ekezet.hurok.buildLogic

import com.vanniktech.maven.publish.MavenPublishBaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

@Suppress("unused")
class PublishingPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.findPlugin("mavenPublish").get().get().pluginId)

        extensions.configure<MavenPublishBaseExtension> {
            configurePublishing()
        }
    }
}

private fun MavenPublishBaseExtension.configurePublishing() {
    publishToMavenCentral(automaticRelease = true)

    signAllPublications()

    pom {
        name.set("hurok")
        description.set("UDF application framework for Kotlin")
        url.set("https://github.com/atomgomba/hurok")

        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }

        developers {
            developer {
                id.set("atomgomba")
                name.set("Károly Kiripolszky")
                email.set("karcsi@ekezet.com")
            }
        }

        scm {
            url.set("https://github.com/atomgomba/hurok")
        }
    }
}
