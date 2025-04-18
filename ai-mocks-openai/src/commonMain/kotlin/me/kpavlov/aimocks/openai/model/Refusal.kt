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

import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A refusal from the model.
 *
 * @param type The type of the refusal. Always `refusal`.
 * @param refusal The refusal explanationfrom the model.
 */
@Serializable
public data class Refusal(
    // The type of the refusal. Always `refusal`.
    @SerialName(value = "type") @Required val type: Refusal.Type,
    // The refusal explanationfrom the model.
    @SerialName(value = "refusal") @Required val refusal: kotlin.String,
) {
    /**
     * The type of the refusal. Always `refusal`.
     *
     * Values: REFUSAL
     */
    @Serializable
    public enum class Type(
        public val value: kotlin.String,
    ) {
        @SerialName(value = "refusal")
        REFUSAL("refusal"),
    }
}
