detekt {
    input = files("src/main/kotlin")
    config = files("detekt.yml")
    baseline = file("config/detekt/baseline.xml")
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