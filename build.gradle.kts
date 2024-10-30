import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.psi.addRemoveModifier.addModifier


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


    // Input directories for Detekt


    val input = projectDir
    val exclude = listOf("**/build/**", "**/resources/**")

    // Set up input and exclusion parameters
    source.setFrom(fileTree(input) {
        exclude(exclude)
    })
    parallel = true // Run Detekt in parallel

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
