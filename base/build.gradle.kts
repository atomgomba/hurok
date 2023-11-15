plugins {
    kotlin("jvm")
    `maven-publish`
}

kotlin {
    sourceSets {
        val main by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines)
            }
        }

        val test by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }

    jvmToolchain(17)
}

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}
