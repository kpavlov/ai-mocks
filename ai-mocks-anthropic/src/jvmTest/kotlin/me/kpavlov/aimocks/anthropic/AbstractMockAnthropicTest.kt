package me.kpavlov.aimocks.anthropic

import io.github.oshai.kotlinlogging.KotlinLogging
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import kotlin.random.Random
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

val anthropic = MockAnthropic(verbose = true)

@OptIn(ExperimentalUuidApi::class)
internal abstract class AbstractMockAnthropicTest {
    protected var temperatureValue: Double = -1.0
    protected lateinit var userIdValue: String
    protected var maxCompletionTokensValue: Long = -1
    protected lateinit var modelName: String
    protected val logger = KotlinLogging.logger(name = this::class.simpleName!!)

    @BeforeEach
    fun beforeEach() {
        modelName =
            arrayOf(
                "claude-3-7-sonnet-latest",
                "claude-3-7-sonnet-20250219",
                "claude-3-5-haiku-latest",
                "claude-3-opus-latest",
            ).random()
        userIdValue = Uuid.random().toHexString()
        temperatureValue = Random.nextDouble(0.0, 1.0)
        maxCompletionTokensValue = Random.nextLong(100, 500)
    }

    @AfterEach
    fun verifyNoUnmatchedRequests() {
        anthropic.verifyNoUnmatchedRequests()
    }
}
