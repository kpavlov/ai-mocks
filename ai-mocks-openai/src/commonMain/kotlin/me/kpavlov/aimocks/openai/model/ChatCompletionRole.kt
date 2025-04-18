/**
 *
 * Please note:
 * This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * Do not edit this file manually.
 *
 */

@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport",
)

package me.kpavlov.aimocks.openai.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The role of the author of a message
 *
 * Values: DEVELOPER,SYSTEM,USER,ASSISTANT,TOOL,FUNCTION
 */
@Serializable
public enum class ChatCompletionRole(
    public val value: kotlin.String,
) {
    @SerialName(value = "developer")
    DEVELOPER("developer"),

    @SerialName(value = "system")
    SYSTEM("system"),

    @SerialName(value = "user")
    USER("user"),

    @SerialName(value = "assistant")
    ASSISTANT("assistant"),

    @SerialName(value = "tool")
    TOOL("tool"),

    @SerialName(value = "function")
    FUNCTION("function"),
    ;

    /**
     * Override [toString()] to avoid using the enum variable name as the value, and instead use
     * the actual value defined in the API spec file.
     *
     * This solves a problem when the variable name and its value are different, and ensures that
     * the client sends the correct enum values to the server always.
     */
    override fun toString(): kotlin.String = value

    public companion object {
        /**
         * Converts the provided [data] to a [String] on success, null otherwise.
         */
        public fun encode(data: kotlin.Any?): kotlin.String? =
            if (data is ChatCompletionRole) "$data" else null

        /**
         * Returns a valid [ChatCompletionRole] for [data], null otherwise.
         */
        public fun decode(data: kotlin.Any?): ChatCompletionRole? =
            data?.let {
                val normalizedData = "$it".lowercase()
                entries.firstOrNull { value ->
                    it == value || normalizedData == "$value".lowercase()
                }
            }
    }
}
