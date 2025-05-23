plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp.plugin)
    alias(libs.plugins.apollo.android)
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
    apollo {
        service("GraphQL") {
            packageName.set("com.data.graphql")
            introspection {
                endpointUrl.set("https://rickandmortyapi.com/graphql")
                schemaFile.set(file("src/main/graphql/schema.sdl"))

            }
        }
    }
}

dependencies {
    implementation(project(":common"))
    implementation(project(":core"))
    implementation(project(":domain"))
    implementation(libs.apollo.runtime)
    implementation(libs.dagger)
    ksp(libs.dagger.compiler)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.turbine)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    testImplementation(libs.mockk)
    testImplementation(libs.mockwebserver)
}
