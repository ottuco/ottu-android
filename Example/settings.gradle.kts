import java.io.FileInputStream
import java.util.Properties

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        flatDir {
            dirs = setOf(file("libs"))
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://jitpack.io")
        }
    }
}

rootProject.name = "ottu-android"
include(":app")

val localPropertiesFile = File(rootDir, "local.properties")
val localProperties = Properties()

if (localPropertiesFile.exists()) {
    FileInputStream(localPropertiesFile).use { stream ->
        localProperties.load(stream)
    }
} else {
    // Optional: it only points at a local SDK checkout. Without it the app uses the published SDK,
    // which is how CI (CodeQL) builds it.
    println("Android Sample - no local.properties at ${localPropertiesFile.absolutePath}, using the published SDK")
}

val ottuSdkPath: String? = localProperties.getProperty("ottuSdk")
println("Android Sample -local.properties, ottuSdk path: $ottuSdkPath")

// Check if :ottu-android-checkout is included as a dependency
val appBuildGradleFile = File(rootProject.projectDir, "app/build.gradle.kts")

if (appBuildGradleFile.exists()) {
    val buildGradleContent = appBuildGradleFile.readLines()
    val isDependencyExist = buildGradleContent.any { line ->
        line.contains("""com.github.ottuco:ottu-android-checkout""") && !line.trim()
            .startsWith("//")
    }
    println("Android Sample - isDependencyExist: $isDependencyExist")
    if (isDependencyExist && ottuSdkPath != null) {
        println("Android Sample - include build: $ottuSdkPath")
        includeBuild("$ottuSdkPath") {
            dependencySubstitution {
                substitute(module("com.github.ottuco:ottu-android-checkout:"))
                    .using(project(":"))
            }
        }
    } else {
        println("Android Sample - doesn't include local SDK: $ottuSdkPath")
    }
}
