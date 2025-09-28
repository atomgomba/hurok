package com.ekezet.hurok.buildLogic.gradleExtensions

import com.vanniktech.maven.publish.MavenPublishBaseExtension
import org.gradle.api.Project

internal fun MavenPublishBaseExtension.configure(target: Project) = with(target) {
    publishToMavenCentral()

    signAllPublications()

    coordinates(group.toString(), name, version.toString())

    pom {
        name.set("hurok framework")
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