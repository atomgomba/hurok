plugins {
    `kotlin-dsl`
}

group = "com.ekezet.hurok.buildlogic"

kotlin {
    compilerOptions {
        jvmToolchain(21)
    }
}

dependencies {
    compileOnly(libs.androidKotlinMultiplatformLibrary.gradlePlugin)
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
