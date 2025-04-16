package me.kpavlov.aimocks.a2a

import io.kotest.matchers.equals.shouldBeEqual
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import me.kpavlov.aimocks.a2a.model.AgentAuthentication
import me.kpavlov.aimocks.a2a.model.AgentCapabilities
import me.kpavlov.aimocks.a2a.model.AgentCard
import me.kpavlov.aimocks.a2a.model.AgentProvider
import me.kpavlov.aimocks.a2a.model.AgentSkill
import me.kpavlov.aimocks.a2a.model.create
import kotlin.test.Test
import kotlin.time.Duration.Companion.milliseconds

internal class AgentCardTest : AbstractTest() {
    @Test
    fun `Should get AgentCard`() =
        runTest {
            val agentCard =
                AgentCard.create {
                    name = "test-agent"
                    description = "test-agent-description"
                    url = a2aServer.baseUrl()
                    documentationUrl = "https://example.com/documentation"
                    version = "0.0.1"
                    provider =
                        AgentProvider(
                            "Acme, Inc.",
                            "https://example.com/organization",
                        )
                    authentication =
                        AgentAuthentication(
                            schemes = listOf("none", "bearer"),
                            credentials = "test-token",
                        )
                    capabilities =
                        AgentCapabilities(
                            streaming = true,
                            pushNotifications = true,
                            stateTransitionHistory = true,
                        )
                    skills =
                        listOf(
                            AgentSkill(
                                id = "walk",
                                name = "Walk the walk",
                            ),
                            AgentSkill(
                                id = "talk",
                                name = "Talk the talk",
                            ),
                        )
                }

            a2aServer.agentCard() responds {
                delay = 1.milliseconds
                card = agentCard
            }

            val response =
                a2aClient
                    .get("/.well-known/agent.json") {
                    }.call
                    .response
                    .body<String>()

            val receivedCard = Json.decodeFromString<AgentCard>(response)
            receivedCard shouldBeEqual agentCard
        }
}
