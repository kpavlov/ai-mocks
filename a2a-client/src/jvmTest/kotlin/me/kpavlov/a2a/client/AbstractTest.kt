package me.kpavlov.a2a.client

import io.github.oshai.kotlinlogging.KotlinLogging
import me.kpavlov.aimocks.a2a.MockAgentServer
import org.junit.jupiter.api.AfterEach

internal abstract class AbstractTest {
    protected val logger = KotlinLogging.logger(name = javaClass.canonicalName!!)
    protected val a2aServer = MockAgentServer(verbose = true)

    @Deprecated("Use client instead")
    protected val a2aClient = createA2AClient(url = a2aServer.baseUrl())
    protected val client = A2AClientFactory.create(baseUrl = a2aServer.baseUrl())

    @AfterEach
    fun afterEach() {
        a2aServer.verifyNoUnmatchedRequests()
    }
}
