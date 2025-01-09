
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.hilt.android) apply false
    id("org.jetbrains.kotlin.kapt") version "2.1.0"
    id("com.google.devtools.ksp") version "2.1.0-1.0.29"
}
// build.gradle.kts (root project)
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        // Add the classpath here
        val nav_version = "2.8.5"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
        classpath("com.android.tools.build:gradle:8.1.4")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.51.1")
    }
}


