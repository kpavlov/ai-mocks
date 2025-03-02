plugins {
    `kotlin-convention`
    `publish-convention`
    alias(libs.plugins.kover) apply true
}

kotlin {

    sourceSets {
        commonMain {
            dependencies {
                api(project(":mokksy"))
                implementation(project.dependencies.platform(libs.ktor.bom))
            }
        }
    }
}
