package com.ekezet.hurok.buildLogic.gradleExtensions

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension
import com.android.build.api.variant.KotlinMultiplatformAndroidComponentsExtension
import com.ekezet.hurok.buildLogic.libs
import org.gradle.api.Project

internal fun KotlinMultiplatformAndroidComponentsExtension.configure(target: Project) = with(target) {
    val suffix = name.removePrefix("hurok-")

    finalizeDsl { extension: KotlinMultiplatformAndroidLibraryExtension ->
        with(extension) {
            namespace = "com.ekezet.hurok.$suffix"
            compileSdk = libs.findVersion("android-compileSdk").get().requiredVersion.toInt()
        }
    }
}