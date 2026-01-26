package com.ekezet.hurok.buildLogic.gradleExtensions

import com.ekezet.hurok.buildLogic.ProjectDefaults
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal fun KotlinMultiplatformExtension.configure() {
    applyDefaultHierarchyTemplate()

    jvm()

    js(IR) {
        browser()
        nodejs()
    }

    compilerOptions {
        jvmToolchain(ProjectDefaults.javaVersion.majorVersion.toInt())

        freeCompilerArgs.addAll(
            "-Xexpect-actual-classes",
        )
    }
}