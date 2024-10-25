import org.jetbrains.kotlin.js.backend.ast.JsEmpty.setSource

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.apollo.android) apply false
    alias(libs.plugins.detekt.plugin) apply true
    alias(libs.plugins.ksp.plugin) apply false
}




detekt {
    setSource(files( // Use 'setSource' instead of 'source ='
        "app/src/main/kotlin",
        "data/src/main/kotlin",
        "domain/src/main/kotlin",
        "presentation/src/main/kotlin"
    ))
    config.setFrom(files("detekt.yml")) // Set configuration file
//    baseline.set(file("config/detekt/baseline.xml")) // Optional baseline file

    parallel = true // Run Detekt in parallel
    buildUponDefaultConfig = true // Build upon default config
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    reports {
        html.required.set(true) // Enable HTML report
        xml.required.set(false)  // Disable XML report
        txt.required.set(false)   // Disable TXT report
    }
}

