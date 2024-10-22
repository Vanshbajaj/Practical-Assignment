plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.detekt.plugin)
    id("kotlin-kapt")
}

android {
    namespace = "com.practical.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    detekt {
        input = files("src/main/kotlin")
        config = files("detekt.yml")
        baseline = file("config/detekt/baseline.xml")
        parallel = true
        buildUponDefaultConfig = true
        parallel = true
        buildUponDefaultConfig = true
    }

    tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        reports {
            html.enabled = true  // Enable HTML report
            xml.enabled = false  // Disable XML report
            txt.enabled = false  // Disable plain text report
        }
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.converter.scalars)
    implementation(libs.apollo.runtime)
    detektPlugins(libs.detekt.formatting)
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)


}