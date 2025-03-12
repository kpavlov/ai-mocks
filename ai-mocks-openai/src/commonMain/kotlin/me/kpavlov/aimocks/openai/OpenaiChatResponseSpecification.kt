package me.kpavlov.aimocks.openai

import kotlinx.coroutines.flow.Flow
import me.kpavlov.aimocks.core.ChatResponseSpecification
import me.kpavlov.mokksy.response.AbstractResponseDefinition
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@Suppress("LongParameterList")
public class OpenaiChatResponseSpecification(
    response: AbstractResponseDefinition<ChatResponse>,
    public var assistantContent: String = "",
    public var responseFlow: Flow<String>? = null,
    public var responseChunks: List<String>? = null,
    public var delayBetweenChunks: Duration = Duration.ZERO,
    public var delay: Duration = Duration.ZERO,
    public var finishReason: String = "stop",
) : ChatResponseSpecification<ChatCompletionRequest, ChatResponse>(response = response) {
    public fun assistantContent(content: String): OpenaiChatResponseSpecification =
        apply {
            this.assistantContent =
                content
        }

    public fun finishReason(finishReason: String): OpenaiChatResponseSpecification =
        apply {
            this.finishReason =
                finishReason
        }

    public fun delayMillis(value: Long): OpenaiChatResponseSpecification =
        apply {
            this.delay = value.milliseconds
        }
}

@Suppress("LongParameterList")
public class OpenaiStreamingChatResponseSpecification(
    response: AbstractResponseDefinition<String>,
    public var responseFlow: Flow<String>? = null,
    public var responseChunks: List<String>? = null,
    public var delayBetweenChunks: Duration = Duration.ZERO,
    public var delay: Duration = Duration.ZERO,
    public var finishReason: String = "stop",
    /**
     * Should send `[DONE]` at the end.
     */
    public var sendDone: Boolean = false,
) : ChatResponseSpecification<ChatCompletionRequest, String>(response = response)
