plugins {
    `kotlin-dsl`
}

group = "com.ekezet.hurok.buildlogic"

dependencies {
    compileOnly(libs.mavenPublish.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("publishingPlugin") {
            id = "hurok.publishing"
            implementationClass = "com.ekezet.hurok.buildLogic.PublishingPlugin"
        }
    }
}
