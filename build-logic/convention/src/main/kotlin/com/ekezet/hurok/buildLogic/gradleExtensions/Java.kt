package com.ekezet.hurok.buildLogic.gradleExtensions

import com.ekezet.hurok.buildLogic.ProjectDefaults
import org.gradle.api.plugins.JavaPluginExtension

internal fun JavaPluginExtension.configure() {
    targetCompatibility = ProjectDefaults.javaVersion
    sourceCompatibility = ProjectDefaults.javaVersion
}