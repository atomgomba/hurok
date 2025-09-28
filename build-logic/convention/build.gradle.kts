plugins {
    `kotlin-dsl`
}

group = "com.ekezet.hurok.buildlogic"

dependencies {
    compileOnly(libs.androidLibrary.gradlePlugin)
    compileOnly(libs.kotlinMultiplatform.gradlePlugin)
    compileOnly(libs.mavenPublish.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("configPlugin") {
            id = "hurok.config"
            implementationClass = "ProjectConfigPlugin"
        }
    }
}
