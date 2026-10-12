// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.ksp) apply false
}

// CodeQL's automatic build runs `testClasses`, a task Android projects don't have (RUL-SD-001, #160364).
// Compile the app instead, so code scanning sees its Kotlin.
tasks.register("testClasses") {
    dependsOn(":app:compileDebugKotlin")
}
