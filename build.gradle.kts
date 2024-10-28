import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.js.backend.ast.JsEmpty.setSource

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.apollo.android) apply false
    alias(libs.plugins.detekt.plugin) apply true
    alias(libs.plugins.ksp.plugin) apply false
    alias(libs.plugins.kotlin.compose.complier) apply false
}


detekt {
    toolVersion = "1.23.7"
    config.setFrom(file("detekt.yml"))
    buildUponDefaultConfig = true
    setSource(files( // Use 'setSource' instead of 'source ='
        "app/src/main/kotlin",
        "data/src/main/kotlin",
        "domain/src/main/kotlin",
        "presentation/src/main/kotlin"
    ))
    parallel = true // Run Detekt in parallel
    buildUponDefaultConfig = true // Build upon default config
}

tasks.withType<Detekt>().configureEach {
    reports {
        xml.required.set(true)
        html.required.set(true)
        txt.required.set(true)
        sarif.required.set(true)
        md.required.set(true)
    }
}
