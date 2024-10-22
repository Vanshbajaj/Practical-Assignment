plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.detekt.plugin)
    alias(libs.plugins.apollo.android)
    id("kotlin-kapt")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
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
    detekt {
        config = files("detekt.yml")  // Path to your detekt.yml file
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
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.0.0")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.apollo.runtime)
    detektPlugins(libs.detekt.formatting)
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)
}