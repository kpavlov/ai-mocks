package me.kpavlov.aimocks.openai

import io.github.oshai.kotlinlogging.KotlinLogging
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import kotlin.random.Random

val openai = MockOpenai(verbose = true)

internal abstract class AbstractMockOpenaiTest {
    protected var temperatureValue: Double = -1.0
    protected var seedValue: Int = -1
    protected var maxCompletionTokensValue: Long = -1
    protected lateinit var modelName: String
    protected val logger = KotlinLogging.logger(name = this::class.simpleName!!)

    @BeforeEach
    fun beforeEach() {
        modelName = arrayOf("gpt-4o", "gpt-4o-mini", "o1", "o1-mini", "o3-mini").random()
        seedValue = Random.nextInt(1, 100500)
        temperatureValue = Random.nextDouble(0.0, 1.0)
        maxCompletionTokensValue = Random.nextLong(100, 500)
    }

    @AfterEach
    fun verifyNoUnmatchedRequests() {
        openai.verifyNoUnmatchedRequests()
    }
}
