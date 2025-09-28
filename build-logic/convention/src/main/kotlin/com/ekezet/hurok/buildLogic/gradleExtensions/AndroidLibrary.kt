package com.ekezet.hurok.buildLogic.gradleExtensions

import com.android.build.api.dsl.LibraryExtension
import com.ekezet.hurok.buildLogic.ProjectDefaults
import com.ekezet.hurok.buildLogic.libs
import org.gradle.api.Project

internal fun LibraryExtension.configure(target: Project) = with(target) {
    namespace = "com.ekezet.hurok"
    compileSdk = libs.findVersion("android-compileSdk").get().requiredVersion.toInt()

    defaultConfig {
        minSdk = libs.findVersion("android-minSdk").get().requiredVersion.toInt()
    }

    compileOptions {
        sourceCompatibility = ProjectDefaults.javaVersion
        targetCompatibility = ProjectDefaults.javaVersion
    }
}