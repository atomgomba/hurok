plugins {
    kotlin("jvm")
    `maven-publish`
}

kotlin {
    sourceSets {
        val main by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines)
                api(libs.kotlinx.coroutines.test)
                implementation(kotlin("test"))
                implementation(project(":base"))
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
