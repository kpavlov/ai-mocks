package me.kpavlov.aimocks.openai.lc4j

import dev.langchain4j.data.message.UserMessage.userMessage
import dev.langchain4j.model.chat.chat
import dev.langchain4j.model.openai.OpenAiChatModel
import dev.langchain4j.model.openai.OpenAiChatRequestParameters
import dev.langchain4j.model.output.FinishReason
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.test.runTest
import me.kpavlov.aimocks.openai.AbstractMockOpenaiTest
import me.kpavlov.aimocks.openai.openai
import kotlin.test.Test

internal class ChatCompletionLc4jTest : AbstractMockOpenaiTest() {
    private val model: OpenAiChatModel =
        OpenAiChatModel
            .builder()
            .apiKey("foo")
            .baseUrl("http://127.0.0.1:${openai.port()}/v1")
            .build()

    @Test
    fun `Should respond to Chat Completion`() =
        runTest {
            openai.completion {
                temperature = temperatureValue
                seed = seedValue
                model = modelName
                maxCompletionTokens = maxCompletionTokensValue
            } responds {
                assistantContent = "Hello"
                finishReason = "stop"
            }

            val result =
                model.chat {
                    parameters =
                        OpenAiChatRequestParameters
                            .builder()
                            .maxCompletionTokens(maxCompletionTokensValue.toInt())
                            .temperature(temperatureValue)
                            .modelName(modelName)
                            .seed(seedValue)
                            .build()
                    messages += userMessage("Say Hello")
                }

            result.apply {
                finishReason() shouldBe FinishReason.STOP
                tokenUsage() shouldNotBe null
                aiMessage().text() shouldBe "Hello"
            }
        }
}
