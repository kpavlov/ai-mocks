package me.kpavlov.aimocks.openai.official.responses

import com.openai.models.responses.ResponseCreateParams
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import me.kpavlov.aimocks.openai.official.AbstractOpenaiTest
import me.kpavlov.aimocks.openai.openai
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.measureTimedValue

internal class ResponsesOpenaiTest : AbstractOpenaiTest() {
    @Test
    fun `Should respond to Responses`() {
        openai.responses {
            temperature = temperatureValue
            model = modelName
            maxTokens = maxCompletionTokensValue
            systemMessageContains("Be ultra-brief.")
            userMessageContains("How to start business?")
        } respond {
            assistantContent = "Find. Create. Sell."
            finishReason = "stop"
            delay = 200.milliseconds
        }

        val params =
            createResponseCreateRequestParams()

        val timedValue =
            measureTimedValue {
                client
                    .responses()
                    .create(params)
            }

        timedValue.duration shouldBeGreaterThan 200.milliseconds
        val result = timedValue.value

        result.validate()

        result.model().asString() shouldBe modelName
        val message = result.output().first().asMessage()
        message
            .content()
            .first()
            .asOutputText()
            .text() shouldBe "Find. Create. Sell."
    }

    private fun createResponseCreateRequestParams(): ResponseCreateParams {
        val params =
            ResponseCreateParams.Companion
                .builder()
                .temperature(temperatureValue)
                .maxOutputTokens(maxCompletionTokensValue)
                .model(modelName)
                .instructions("Be ultra-brief.")
                .input("How to start business?")
                .build()
        return params
    }
}
