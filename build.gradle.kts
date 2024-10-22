plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.apollo.android) apply false
    alias(libs.plugins.detekt.plugin) apply true  // Apply Detekt globally
}




        detekt {
            input = files("src/main/kotlin") // Path to Kotlin source files
            config = files("detekt.yml") // Path to configuration file
            baseline = file("config/detekt/baseline.xml") // Path to baseline (optional)
            parallel = true // Run detekt in parallel
            buildUponDefaultConfig = true // Build upon default config
        }

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    reports {
        html.enabled = true
        xml.enabled = false
        txt.enabled = false
    }
}
