import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.ksp)
    alias(libs.plugins.datadog)
    id("org.jetbrains.kotlin.plugin.compose")
}

val merchantProperties = Properties().apply {
    rootProject.file("local.properties").takeIf { it.exists() }?.inputStream()?.use { load(it) }
}

fun merchantSetting(flavor: String, name: String): String {
    fun String.environmentName() = replace(Regex("([a-z])([A-Z])"), "$1_$2").uppercase()
    val environmentName = "OTTU_${flavor.environmentName()}_${name.environmentName()}"
    return providers.environmentVariable(environmentName).orNull
        ?: merchantProperties.getProperty("ottu.$flavor.$name", "")
}

fun String.asBuildConfigString() = "\"" + replace("\\", "\\\\")
    .replace("\"", "\\\"")
    .replace("\n", "\\n")
    .replace("\r", "\\r") + "\""

android {
    namespace = "com.ottu"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.ottu"
        minSdk = 26
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        signingConfig = signingConfigs.getByName("debug")
    }

    flavorDimensions += "merchant"
    productFlavors {
        listOf("stage", "alpha", "alphaBulk").forEach { merchant ->
            create(merchant) {
                dimension = "merchant"
                isDefault = merchant == "alpha"
                buildConfigField("String", "MERCHANT_ID", merchantSetting(merchant, "merchantId").asBuildConfigString())
                buildConfigField("String", "API_KEY", merchantSetting(merchant, "apiKey").asBuildConfigString())
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17) // Use the appropriate JVM version
        }
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
        compose = true
    }
    packaging {
        resources.excludes.add("META-INF/versions/9/OSGI-INF/MANIFEST.MF")
    }
}

dependencies {
    implementation("com.github.ottuco:ottu-android-checkout:2.3.0")


    implementation(libs.datadog.rum)
    implementation(libs.datadog.session.replay)

    implementation(libs.colorpickerview)
    implementation(libs.expandablelayout)
    implementation(libs.numberpicker)

    implementation(libs.androidx.core.ktx)
    implementation(libs.material)

    implementation(libs.koin)

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter)
    implementation(libs.moshi)
    implementation(libs.okhttp.logging)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.runtime)

    val composeBom = platform("androidx.compose:compose-bom:2024.10.01")
    implementation(composeBom)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation("androidx.fragment:fragment-compose:1.8.5")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
