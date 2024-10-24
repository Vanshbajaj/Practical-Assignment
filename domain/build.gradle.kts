plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.apollo.android)
    alias(libs.plugins.ksp.plugin)
}
subprojects{
    apply(from = "detekt-config.gradle.kts")
}
android {
    namespace = "com.practical.domain"
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
            packageName.set("com.domain.graphql")
            introspection {
                endpointUrl.set("https://rickandmortyapi.com/graphql")
                schemaFile.set(file("src/main/graphql/schema.sdl"))

            }
        }
    }

}


dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.apollo.runtime)
  
}