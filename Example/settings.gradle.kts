import java.io.FileInputStream
import java.io.FileNotFoundException
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
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "ottu-android-checkout-sample"
include(":app")

val localPropertiesFile = File(rootDir, "local.properties")
val localProperties = Properties()

if (localPropertiesFile.exists()) {
    FileInputStream(localPropertiesFile).use { stream ->
        localProperties.load(stream)
    }
} else {
    throw FileNotFoundException("local.properties file not found at ${localPropertiesFile.absolutePath}")
}

val ottuSdkPath: String? = localProperties.getProperty("ottuSdk")

// Check if :ottu-android-checkout is included as a dependency
val appBuildGradleFile = File(rootProject.projectDir, "app/build.gradle.kts")

if (appBuildGradleFile.exists()) {
    val buildGradleContent = appBuildGradleFile.readLines()
    val isDependencyExist = buildGradleContent.any { line ->
        line.contains("""implementation(project(":ottu-android-checkout"))""") && !line.trim()
            .startsWith("//")
    }
    if (isDependencyExist) {
        if (ottuSdkPath == null) {
            throw GradleException(
                "Property 'ottuSdk' not found in local.properties. " +
                        "Use this: \"ottuSdk=/path/to/your/ottu-android-checkout/app\" with the actual path to your module."
            )
        }
        include(":ottu-android-checkout")
        project(":ottu-android-checkout").projectDir = File(ottuSdkPath)
    }
}
