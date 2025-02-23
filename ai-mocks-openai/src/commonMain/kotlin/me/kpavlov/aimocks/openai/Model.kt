@file:OptIn(ExperimentalSerializationApi::class)

package me.kpavlov.aimocks.openai

import kotlinx.serialization.Contextual
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.EncodeDefault.Mode.ALWAYS
import kotlinx.serialization.EncodeDefault.Mode.NEVER
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class Chunk(
    val id: String,
    /**
     * Always "chat.completion.chunk"
     */
    @SerialName("object")
    @EncodeDefault(ALWAYS)
    val objectType: String = "chat.completion.chunk",
    val created: Long,
    val model: String,
    @SerialName("service_tier")
    val serviceTier: String? = null,
    @SerialName("system_fingerprint")
    val systemFingerprint: String,
    val usage: Usage? = null,
    val choices: List<Choice>,
)

@Serializable
public data class Choice(
    val index: Int,
    @EncodeDefault(NEVER)
    val delta: Delta? = null,
    @EncodeDefault(NEVER)
    val message: Message? = null,
    @EncodeDefault(ALWAYS)
    val logprobs: String? = null,
    @EncodeDefault(ALWAYS)
    @SerialName("finish_reason")
    val finishReason: String? = null,
)

@Serializable
public data class Delta(
    val role: String? = null,
    val content: String? = null,
)

@Serializable
public data class ChatResponse(
    val id: String,
    @EncodeDefault(ALWAYS)
    @SerialName("object")
    val objectType: String = "chat.completion",
    val created: Long,
    val model: String,
    @SerialName("service_tier")
    val serviceTier: String? = null,
    @SerialName("system_fingerprint")
    val systemFingerprint: String? = null,
    val usage: Usage,
    val choices: List<Choice>,
)

@Serializable
public data class Usage(
    @SerialName("prompt_tokens")
    val promptTokens: Int,
    @SerialName("completion_tokens")
    val completionTokens: Int,
    @SerialName("total_tokens")
    val totalTokens: Int,
    @SerialName("completion_tokens_details")
    val completionTokensDetails: CompletionTokensDetails,
    @SerialName("prompt_tokens_details")
    val promptTokensDetails: TokenDetails? = null,
)

@Serializable
public data class CompletionTokensDetails(
    @SerialName("reasoning_tokens")
    val reasoningTokens: Int,
    @SerialName("accepted_prediction_tokens")
    val acceptedPredictionTokens: Int,
    @SerialName("rejected_prediction_tokens")
    val rejectedPredictionTokens: Int,
)

@Serializable
public data class TokenDetails(
    @SerialName("cached_tokens")
    val cachedTokens: Int? = null,
)

@Serializable
public data class Metadata(
    val tags: Map<String, String>? = null,
)

/**
 * Represents a request for generating a chat-based completion in an OpenAI-like environment.
 * See [Create chat completion](https://platform.openai.com/docs/api-reference/chat/create).
 *
 * This data class is used for serialization and defines the parameters required to send
 * a chat completion request, including the input messages, model to use, and various tuning parameters.
 *
 * @property messages A list of input messages that define the conversation context, each with a role and content.
 * @property model The identifier of the language model to be used for generating the completion.
 * @property store A flag indicating whether the conversation context should be stored for further use.
 * @property reasoningEffort Specifies the level of computational effort to apply during reasoning
 * ("low", "medium", "high").
 * @property metadata Optional metadata associated with the request, such as tags.
 * @property maxCompletionTokens The maximum number of tokens allowed in the generated completion.
 * @property frequencyPenalty The penalty value for repetitive token usage in the response.
 * @property responseFormat Defines the response format, including optional JSON schema support.
 * @property temperature A value between 0.0 and 1.0 that controls the randomness of the generated response.
 * @property seed Can be used to produce deterministic responses in testing.
 */
@Serializable
public data class ChatCompletionRequest(
    val messages: List<Message>,
    val model: String,
    val store: Boolean = false,
    @SerialName("reasoning_effort")
    val reasoningEffort: String = "medium",
    val metadata: Metadata? = null,
    @SerialName("max_completion_tokens")
    val maxCompletionTokens: Int? = null,
    @SerialName("frequency_penalty")
    val frequencyPenalty: Double? = 0.0,
    @SerialName("response_format")
    val responseFormat: ResponseFormat? = null,
    val temperature: Double = 1.0,
    val seed: Int? = null,
)

@Serializable
public data class Message(
    val role: String,
    val content: String,
    val refusal: String? = null,
    @SerialName("tool_calls")
    val toolCalls: List<ToolCall>? = null,
)

@Serializable
public data class ToolCall(
    val id: String,
    @EncodeDefault(ALWAYS)
    val type: String = "function",
    val function: CallableFunction,
)

/**
 * Represents a function that can be called by the AI model.
 *
 * @property name The name of the function.
 * @property arguments The arguments to call the function with,
 * as generated by the model in JSON format.
 * Note that the model does not always generate valid JSON,
 * and may hallucinate parameters not defined by your function schema.
 * Validate the arguments in your code before calling your function.
 */
@Serializable
public data class CallableFunction(
    val name: String,
    val arguments: String,
)

// Add new parameters like JSON Schema support if required
@Serializable
public data class ResponseFormat(
    val type: String,
    @SerialName("json_schema")
    val jsonSchema: Map<String, @Contextual Any>? = null,
)
