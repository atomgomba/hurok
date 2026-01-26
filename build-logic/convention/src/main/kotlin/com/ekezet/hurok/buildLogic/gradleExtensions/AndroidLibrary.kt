package com.ekezet.hurok.buildLogic.gradleExtensions

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension
import com.android.build.api.variant.KotlinMultiplatformAndroidComponentsExtension
import com.ekezet.hurok.buildLogic.libs
import org.gradle.api.Project

internal fun KotlinMultiplatformAndroidComponentsExtension.configure(target: Project) = with(target) {
    finalizeDsl { extension: KotlinMultiplatformAndroidLibraryExtension ->
        with(extension) {
            namespace = "com.ekezet.hurok"
            compileSdk = libs.findVersion("android-compileSdk").get().requiredVersion.toInt()
        }
    }
}