package me.kpavlov.aimocks.anthropic.official

import com.anthropic.client.AnthropicClient
import com.anthropic.client.okhttp.AnthropicOkHttpClient
import com.anthropic.models.MessageCreateParams
import me.kpavlov.aimocks.anthropic.anthropic
import kotlin.test.Test
import kotlin.time.Duration.Companion.milliseconds

internal class MessagesTest : AbstractAnthropicTest() {
    @Test
    fun `Should respond with error`() {
        val client: AnthropicClient =
            AnthropicOkHttpClient
                .builder()
                .apiKey("my-anthropic-api-key")
                .baseUrl(anthropic.baseUrl())
                .responseValidation(true)
                .build()

        anthropic.messages {
            temperature = 0.42
            model = "claude-3-7-sonnet-latest"
            maxCompletionTokens = 100
            systemMessageContains("helpful assistant")
            userMessageContains("say 'Hello!'")
        } responds {
            assistantContent = "Hello"
            delay = 200.milliseconds
        }

        val params =
            MessageCreateParams
                .builder()
                .temperature(0.42)
                .maxTokens(100)
                .system("You are a helpful assistant.")
                .addUserMessage("Just say 'Hello!' and nothing else")
                .model("claude-3-7-sonnet-latest")
                .build()

        val result =
            client
                .messages()
                .create(params)

        val response =
            result
                .content()
                .first()
                .asText()
                .text()

        println("LLM Reponse: $response")
    }
}
