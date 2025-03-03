plugins {
    kotlin("plugin.serialization") apply true
    alias(libs.plugins.kover) apply true
    `kotlin-convention`
    `publish-convention`
}

kotlin {

    sourceSets {
        commonMain {
            dependencies {
                api(libs.kotest.assertions.core)
                api(libs.kotest.assertions.json)
                api(libs.ktor.server.core)
                implementation(libs.ktor.server.double.receive)
                api(libs.ktor.server.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.ktor.server.sse)
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.assertk)
                implementation(libs.datafaker)
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.client.core)
                implementation(libs.mockk)
                implementation(libs.mockk.dsl)
                implementation(libs.kotlinLogging)
            }
        }

        jvmMain {
            dependencies {
                implementation(libs.ktor.server.netty)
                implementation(libs.ktor.server.call.logging)
            }
        }

        jvmTest {
            dependencies {
                implementation(libs.ktor.client.java)
                implementation(libs.junit.jupiter.params)
                runtimeOnly(libs.slf4j.simple)
                implementation(libs.ktor.serialization.jackson)
            }
        }
    }
}
