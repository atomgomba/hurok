rootProject.name = "hurok"

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenLocal()
        mavenCentral()
    }
}

includeBuild("build-logic")

include(":base")
include(":android-compose")
include(":test")
